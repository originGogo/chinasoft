package com.gogo.psy.user.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gogo.psy.common.Redis.IRedisProxy;
import com.gogo.psy.common.util.AuthContextHolder;
import com.gogo.psy.common.Result.ResultCodeEnum;
import com.gogo.psy.common.constant.RedisConstant;
import com.gogo.psy.common.exception.BizException;
import com.gogo.psy.user.config.CosConfig;
import com.gogo.psy.user.config.PasswordUtil;
import com.gogo.psy.user.pojo.dto.*;
import com.gogo.psy.user.mapper.UserRoleMapper;
import com.gogo.psy.common.pojo.UserInfo;
import com.gogo.psy.user.pojo.model.UserAccount;
import com.gogo.psy.user.pojo.model.UserRole;
import com.gogo.psy.user.service.UserBankAccountService;
import com.gogo.psy.user.service.UserInfoService;
import com.gogo.psy.user.mapper.UserInfoMapper;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.qcloud.cos.demo.ci.ClientUtils.getCosClient;

/**
* @author Administrator
* @description 针对表【user_info(用户表)】的数据库操作Service实现
* @createDate 2024-10-21 17:04:30
*/
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Resource
    private WxMaService wxMaService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private IRedisProxy redisProxy;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private CosConfig cosConfig;
    @Resource
    private UserBankAccountService userBankAccountService;

    @Override
    public String firstLogin(String code, String avatarUrl) {
        String openId = null;
        try{
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            openId = sessionInfo.getOpenid();
        } catch (WxErrorException e) {
            throw new BizException(ResultCodeEnum.DATA_ERROR.getCode(), e.getMessage());
        }
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getWxOpenId, openId);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        if (userInfo == null) {
            throw new BizException(ResultCodeEnum.LOGIN_FIRST);
        }
        userInfo.setAvatarUrl(avatarUrl);
        userInfo.setUpdateTime(new Date());
        userInfoMapper.updateById(userInfo);
        String token = UUID.randomUUID().toString().replace("-", "");
        redisProxy.setex(RedisConstant.USER_LOGIN_KEY_PREFIX + token, 60*60*24, JSON.toJSONString(userInfo));
        return token;
    }

    @Override
    public String secondLogin(LoginInfoDTO loginInfoDTO) {
        //判断该账户是否存在
        UserInfo userInfo = checkAccount(loginInfoDTO);
        userInfo.setAvatarUrl(loginInfoDTO.getAvatarUrl());
        String code = loginInfoDTO.getCode();
        try{
            // 绑定微信
            updateUserInfo(code, userInfo);
        }catch (Exception e){
            throw new BizException(ResultCodeEnum.DATA_ERROR);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        redisProxy.setex(RedisConstant.USER_LOGIN_KEY_PREFIX + token, 60*60*24, JSON.toJSONString(userInfo));
        return token;
    }

    @Override
    public UserInfoDTO getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        String info = redisProxy.get(RedisConstant.USER_LOGIN_KEY_PREFIX + token);
        if (info == null){
            throw new BizException(ResultCodeEnum.LOGIN_TIMEOUT);
        }
        UserInfo userInfo = JSON.parseObject(info, UserInfo.class);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setNickname(userInfo.getNickname());
        userInfoDTO.setGender(userInfo.getGender());
        userInfoDTO.setAvatarUrl(userInfo.getAvatarUrl());
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userInfo.getId());
        UserRole userRole = userRoleMapper.selectOne(wrapper);
        if (userRole == null){
            throw new BizException("该用户无绑定角色");
        }
        userInfoDTO.setRoleId(userRole.getRoleId());
        return userInfoDTO;
    }


    public UserInfo checkAccount(LoginInfoDTO loginInfoDTO){
        String account = loginInfoDTO.getAccount();
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getAccount, account);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        if (userInfo == null){
            throw new BizException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        String password = userInfo.getPassword();
        String salt = userInfo.getSalt();
        Boolean check = PasswordUtil.checkPassword(loginInfoDTO.getPassword(), password, salt);
        if (!check){
            throw new BizException(ResultCodeEnum.PASSWORD_ERROR);
        }
        return userInfo;
    }

    public void updateUserInfo(String code, UserInfo userInfo) {

        // 获取code值，使用微信工具包对象，获取微信唯一标识openId
        String openId = null;
        try{
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            openId = sessionInfo.getOpenid();
        } catch (WxErrorException e) {
            throw new BizException(ResultCodeEnum.FAIL);
        }
        userInfo.setWxOpenId(openId);
        userInfo.setUpdateTime(new Date());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        UserInfo userInfo = AuthContextHolder.getUserInfo();
        if (!PasswordUtil.checkPassword(updatePasswordDTO.getOldPassword(), userInfo.getPassword(), userInfo.getSalt())){
            throw new BizException("输入旧密码错误");
        }
        userInfo.setPassword(PasswordUtil.generatePassword(updatePasswordDTO.getNewPassword(), userInfo.getSalt()));
        userInfo.setUpdateTime(new Date());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public List<Long> getIdByNickName(String nickName) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(UserInfo::getNickname, nickName);
        return userInfoMapper.selectList(wrapper).stream().map(UserInfo::getId).collect(Collectors.toList());
    }

    @Override
    public void logout(LogOutDTO logOutDTO) {
        redisProxy.remove(RedisConstant.USER_LOGIN_KEY_PREFIX + logOutDTO.getToken());
    }

    @Override
    public List<UserInfo> getUserByUserId(List<Long> userIds) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserInfo::getId, userIds);
        return userInfoMapper.selectList(wrapper);
    }

    private COSClient getCosClient() {
        COSCredentials cred = new BasicCOSCredentials(cosConfig.getSecretId(), cosConfig.getSecretKey());
        Region region = new Region(cosConfig.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cred, clientConfig);
    }

    @Override
    @SneakyThrows
    public String uploadImg(MultipartFile file, String path) {
        COSClient cosClient = getCosClient();

        //元数据信息
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(file.getContentType());
        meta.setContentLength(file.getSize());
        meta.setContentEncoding("UTF-8");

        String fileType = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String uploadPath =  path + "/" + UUID.randomUUID().toString().replaceAll("-", "") + fileType;
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosConfig.getBucketName(), uploadPath, file.getInputStream(), meta);
        putObjectRequest.setStorageClass(StorageClass.Standard);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        cosClient.shutdown();

        return uploadPath;
    }

    @Override
    public Integer getBalance(Long userId) {
        UserInfo userInfo = AuthContextHolder.getUserInfo();
        Long uid = userInfo.getId();
        UserAccount byUid = userBankAccountService.findByUid(uid);
        return byUid.getBalance();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transfer(TransferDTO transferDTO) {
        if (transferDTO.getAmount() <= 0){
            throw new BizException("转账金额必须大于0");
        }
        UserAccount fromAccount = userBankAccountService.findByBankAccount(transferDTO.getFromAccount());
        UserAccount toAccount = userBankAccountService.findByBankAccount(transferDTO.getToAccount());
        if (fromAccount == null || toAccount == null){
            throw new BizException("账户不存在");
        }
        if (fromAccount.getBalance() < transferDTO.getAmount()){
            throw new BizException("余额不足");
        }
        //转账
        fromAccount.setBalance(fromAccount.getBalance() - transferDTO.getAmount());
        userBankAccountService.save(fromAccount);

        //收款
        toAccount.setBalance(toAccount.getBalance() + transferDTO.getAmount());
        userBankAccountService.save(toAccount);
    }
}





package com.gogo.psy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gogo.psy.common.pojo.UserInfo;
import com.gogo.psy.common.pojo.dto.PageRespDTO;
import com.gogo.psy.common.util.AuthContextHolder;
import com.gogo.psy.user.mapper.UserFeedbackMapper;
import com.gogo.psy.user.pojo.dto.SendReplyDTO;
import com.gogo.psy.user.pojo.dto.UserFeedbackDTO;
import com.gogo.psy.user.pojo.dto.UserFeedbackSendDTO;
import com.gogo.psy.user.pojo.model.UserFeedback;
import com.gogo.psy.user.service.RoleService;
import com.gogo.psy.user.service.UserFeedbackService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
* @author Administrator
* @description 针对表【user_feedback(管理员消息表)】的数据库操作Service实现
* @createDate 2024-10-31 16:49:44
*/
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback>
    implements UserFeedbackService{

    @Resource
    private UserFeedbackMapper userFeedbackMapper;
    @Resource
    private RoleService roleService;

    @Override
    public void sendFeedback(UserFeedbackSendDTO userFeedbackSendDTO) {
        UserFeedback userFeedback = new UserFeedback();
        UserInfo userInfo = AuthContextHolder.getUserInfo();
        userFeedback.setUserId(userInfo.getId());
        Long roleId = roleService.findRoleIdByUserId(userInfo.getId());
        userFeedback.setRoleId(roleId);
        userFeedback.setMessage(userFeedbackSendDTO.getMessage());
        userFeedbackMapper.insert(userFeedback);
    }

    @Override
    public PageRespDTO<UserFeedbackDTO> getFeedback(Integer page, Integer size, String name, Integer roleId) {
        if (page == null){
            page = 1;
        }
        if (size == null){
            size = 10;
        }
        Page<UserFeedbackDTO> startPage = PageHelper.startPage(page, size);
        List<UserFeedbackDTO> userFeedbacks = userFeedbackMapper.selectByPage(name, roleId);

        if (CollectionUtils.isEmpty(userFeedbacks)){
            return PageRespDTO.of(startPage.getTotal(), new ArrayList<>());
        }
        return PageRespDTO.of(startPage.getTotal(), userFeedbacks);
    }

    @Override
    public void sendReply(SendReplyDTO sendReplyDTO) {
        LambdaQueryWrapper<UserFeedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFeedback::getId, sendReplyDTO.getFeedbackId());
        UserFeedback userFeedback = userFeedbackMapper.selectOne(queryWrapper);
        userFeedback.setReply(sendReplyDTO.getReply());
        userFeedback.setReplyTime(new Date());
        userFeedbackMapper.updateById(userFeedback);
    }

    @Override
    public List<UserFeedback> getByUserId() {
        UserInfo userInfo = AuthContextHolder.getUserInfo();
        Long userId = userInfo.getId();
        LambdaQueryWrapper<UserFeedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFeedback::getUserId, userId);
        return userFeedbackMapper.selectList(queryWrapper);
    }

}





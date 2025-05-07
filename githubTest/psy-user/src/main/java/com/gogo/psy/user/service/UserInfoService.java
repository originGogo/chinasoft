package com.gogo.psy.user.service;

import com.gogo.psy.user.pojo.dto.*;
import com.gogo.psy.common.pojo.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2024-10-21 17:04:30
*/
public interface UserInfoService extends IService<UserInfo> {

    String firstLogin(String code, String avatarUrl);

    String secondLogin(LoginInfoDTO loginInfoDTO);

    UserInfoDTO getUserInfo(HttpServletRequest request);

    void updatePassword(UpdatePasswordDTO updatePasswordDTO);

    List<Long> getIdByNickName(String nickName);

    void logout(LogOutDTO logOutDTO);

    List<UserInfo> getUserByUserId(List<Long> userIds);

    String uploadImg(MultipartFile file, String path);

    Integer getBalance(Long userId);

    void transfer(TransferDTO transferDTO);
}

package com.gogo.psy.user.service;

import com.gogo.psy.common.pojo.dto.PageRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gogo.psy.user.pojo.dto.SendReplyDTO;
import com.gogo.psy.user.pojo.dto.UserFeedbackDTO;
import com.gogo.psy.user.pojo.dto.UserFeedbackSendDTO;
import com.gogo.psy.user.pojo.model.UserFeedback;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user_feedback(管理员消息表)】的数据库操作Service
* @createDate 2024-10-31 16:49:44
*/
public interface UserFeedbackService extends IService<UserFeedback> {

    void sendFeedback(UserFeedbackSendDTO userFeedbackSendDTO);

    PageRespDTO<UserFeedbackDTO> getFeedback(Integer page, Integer size, String name, Integer roleId);

    void sendReply(SendReplyDTO sendReplyDTO);

    List<UserFeedback> getByUserId();
}

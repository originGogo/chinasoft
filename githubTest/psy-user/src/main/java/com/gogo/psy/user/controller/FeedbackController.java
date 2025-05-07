package com.gogo.psy.user.controller;

import com.gogo.psy.common.Result.Result;
import com.gogo.psy.common.login.GogoLogin;
import com.gogo.psy.common.pojo.dto.PageRespDTO;
import com.gogo.psy.user.pojo.dto.SendReplyDTO;
import com.gogo.psy.user.pojo.dto.UserFeedbackDTO;
import com.gogo.psy.user.pojo.dto.UserFeedbackSendDTO;
import com.gogo.psy.user.pojo.model.UserFeedback;
import com.gogo.psy.user.service.UserFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Resource
    private UserFeedbackService userFeedbackService;

    @PostMapping("/user")
    @GogoLogin
    @Operation(summary = "发送反馈")
    public Result sendFeedback(@RequestBody UserFeedbackSendDTO userFeedbackSendDTO) {
        userFeedbackService.sendFeedback(userFeedbackSendDTO);
        return Result.ok();
    }

    @GetMapping("/admin")
    @GogoLogin
    @Operation(summary = "接收反馈")
    public Result<PageRespDTO<UserFeedbackDTO>> getFeedback(@RequestParam(name = "page", required = false) Integer page,
                                                          @RequestParam(name = "size", required = false) Integer size,
                                                          @RequestParam(name = "name", required = false) String name,
                                                          @RequestParam(name = "roleId", required = false) Integer roleId) {
        PageRespDTO<UserFeedbackDTO> userFeedback = userFeedbackService.getFeedback(page, size, name, roleId);
        return Result.ok(userFeedback);
    }

    @PostMapping("/manager")
    @GogoLogin
    @Operation(summary = "发送回复")
    public Result sendReply(@RequestBody SendReplyDTO sendReplyDTO){
        userFeedbackService.sendReply(sendReplyDTO);
        return Result.ok();
    }

    @GetMapping("/user/reply")
    @GogoLogin
    @Operation(summary = "接收回复")
    public Result<List<UserFeedback>> getReply() {
        List<UserFeedback> res = userFeedbackService.getByUserId();
        return Result.ok(res);
    }
}

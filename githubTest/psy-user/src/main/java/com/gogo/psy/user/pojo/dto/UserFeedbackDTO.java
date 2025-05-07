package com.gogo.psy.user.pojo.dto;

import com.gogo.psy.user.pojo.model.UserFeedback;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFeedbackDTO extends UserFeedback {

    @Schema(description = "发送反馈人的名字")
    private String nickname;
}

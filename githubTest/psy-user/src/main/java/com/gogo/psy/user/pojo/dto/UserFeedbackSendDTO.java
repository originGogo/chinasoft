package com.gogo.psy.user.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFeedbackSendDTO {

    @Schema(description = "反馈")
    private String message;
}

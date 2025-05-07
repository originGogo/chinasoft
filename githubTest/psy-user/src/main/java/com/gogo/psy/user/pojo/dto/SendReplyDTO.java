package com.gogo.psy.user.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SendReplyDTO {

    @Schema(description = "回复消息")
    private String reply;
    @Schema(description = "feedback")
    private Long feedbackId;
}

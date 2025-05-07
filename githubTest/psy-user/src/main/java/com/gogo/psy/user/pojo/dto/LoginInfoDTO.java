package com.gogo.psy.user.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginInfoDTO {

    @Schema(description = "账号")
    private String account;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "识别码")
    private String code;
    @Schema(description = "头像")
    private String avatarUrl;

}

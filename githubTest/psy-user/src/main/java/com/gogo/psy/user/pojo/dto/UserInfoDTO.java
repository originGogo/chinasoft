package com.gogo.psy.user.pojo.dto;

import lombok.Data;

@Data
public class UserInfoDTO {

    private String nickname;
    private Long roleId;
    private String gender;
    private String avatarUrl;

}

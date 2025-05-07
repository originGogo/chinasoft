package com.gogo.psy.user.service;

import com.gogo.psy.user.pojo.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【role(角色表)】的数据库操作Service
* @createDate 2024-10-31 12:19:57
*/
public interface RoleService extends IService<Role> {

    Long findRoleIdByUserId(Long userId);
}

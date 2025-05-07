package com.gogo.psy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gogo.psy.user.mapper.UserRoleMapper;
import com.gogo.psy.user.pojo.model.Role;
import com.gogo.psy.user.pojo.model.UserRole;
import com.gogo.psy.user.service.RoleService;
import com.gogo.psy.user.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2024-10-31 12:19:57
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Long findRoleIdByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        UserRole userRole = userRoleMapper.selectOne(queryWrapper);
        return userRole.getRoleId();
    }
}





package com.gogo.psy.user.mapper;

import com.gogo.psy.user.pojo.model.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【user_role(用户角色表)】的数据库操作Mapper
* @createDate 2024-10-31 12:20:58
* @Entity com.gogo.psy.psyuser.pojo.model.UserRole
*/
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}





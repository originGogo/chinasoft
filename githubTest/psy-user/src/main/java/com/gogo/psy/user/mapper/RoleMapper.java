package com.gogo.psy.user.mapper;

import com.gogo.psy.user.pojo.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【role(角色表)】的数据库操作Mapper
* @createDate 2024-10-31 12:19:57
* @Entity com.gogo.psy.psyuser.pojo.model.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}





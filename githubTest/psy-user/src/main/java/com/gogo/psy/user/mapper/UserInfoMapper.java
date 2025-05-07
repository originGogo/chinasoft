package com.gogo.psy.user.mapper;

import com.gogo.psy.common.pojo.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【user_info(用户表)】的数据库操作Mapper
* @createDate 2024-10-21 17:04:30
* @Entity com.gogo.psy.psyuser.pojo.model.UserInfo
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}





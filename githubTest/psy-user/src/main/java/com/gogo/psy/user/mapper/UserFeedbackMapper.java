package com.gogo.psy.user.mapper;

import com.gogo.psy.user.pojo.dto.UserFeedbackDTO;
import com.gogo.psy.user.pojo.model.UserFeedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user_feedback(反馈表)】的数据库操作Mapper
* @createDate 2024-11-05 16:00:18
* @Entity com.gogo.psy.psyuser.pojo.model.UserFeedback
*/
@Mapper
public interface UserFeedbackMapper extends BaseMapper<UserFeedback> {

    List<UserFeedbackDTO> selectByPage(@Param("name") String name,@Param("roleId")Integer roleId);
}





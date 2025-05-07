package com.gogo.psy.user.pojo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 反馈表
 * @TableName user_feedback
 */
@TableName(value ="user_feedback")
@Data
public class UserFeedback implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 身份id
     */
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 消息
     */
    @TableField(value = "message")
    private String message;

    /**
     * 回复消息
     */
    @TableField(value = "reply")
    private String reply;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date createTime;

    /**
     * 回复时间
     */
    @TableField(value = "reply_time")
    private Date replyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
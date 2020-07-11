package com.chen.stencil.mbg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author chen
 * @since 2020-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Users对象", description="会员表")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "电话号码")
    private Integer telephone;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "帐号启用状态：0->禁用；1->启用")
    private Integer status;

}

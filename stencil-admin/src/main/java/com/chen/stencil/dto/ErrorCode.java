package com.chen.stencil.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaobozi
 */
@Getter
@Setter
public class ErrorCode {


    @ApiModelProperty("错误代码")
    private Integer code;
    @ApiModelProperty("错误描述")
    private String message;

}

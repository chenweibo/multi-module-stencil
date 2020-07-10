package com.chen.stencil.controller;


import cn.hutool.core.util.EnumUtil;
import com.chen.stencil.common.response.ResultCode;
import com.chen.stencil.dto.ErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 返回所有业务错误状态码
 * @author xiaobozi
 */
@RestController
@Api(tags = "异常返回状态列表")
public class ErrorCodeController {

    @ApiOperation("异常码查询")
    @ResponseBody
    @RequestMapping(value = "/error", method = RequestMethod.GET, produces = "application/json")
    public List<ErrorCode> error() {
        List<String> names = EnumUtil.getNames(ResultCode.class);
        Map<String, Object> code = EnumUtil.getNameFieldMap(ResultCode.class, "code");
        Map<String, Object> message = EnumUtil.getNameFieldMap(ResultCode.class, "message");
        List<ErrorCode> result = new ArrayList<>();
        for (String item : names
        ) {
            ErrorCode errorCode = new ErrorCode();
            errorCode.setMessage(message.get(item).toString());
            errorCode.setCode(Integer.parseInt(code.get(item).toString()));
            result.add(errorCode);

        }
        return result;
    }

}

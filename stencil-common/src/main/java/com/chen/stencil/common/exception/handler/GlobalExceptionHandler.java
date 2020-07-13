package com.chen.stencil.common.exception.handler;

import com.chen.stencil.common.exception.ApiException;
import com.chen.stencil.common.response.CommonResult;
import com.chen.stencil.common.response.IErrorCode;
import com.chen.stencil.common.response.ResultCode;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResult handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public CommonResult uploadExecption() {

        return CommonResult.failed(ResultCode.BIG_SIZE);
    }


    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public CommonResult ParameterExecption() {

        return CommonResult.failed(ResultCode.VALIDATE_PARAMS);
    }
}

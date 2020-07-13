package com.chen.stencil.user.controller;


import cn.hutool.core.lang.Validator;
import com.chen.stencil.common.response.CommonResult;
import com.chen.stencil.common.response.IErrorCode;
import com.chen.stencil.common.response.ResultCode;
import com.chen.stencil.mbg.model.Users;
import com.chen.stencil.user.component.MailMessageSender;
import com.chen.stencil.user.component.TelephoneMessageSender;
import com.chen.stencil.user.service.IUsersService;
import com.chen.stencil.user.service.UserCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员登录注册管理Controller
 * Created by macro on 2018/8/3.
 */
@RestController
@Api(tags = "会员登录注册管理")
@RequestMapping("/sso")
public class UserController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private IUsersService usersService;
    @Autowired
    UserCacheService userCacheService;
    @Autowired
    TelephoneMessageSender telephoneMessageSender;

    @Autowired
    MailMessageSender mailMessageSender;

    @ApiOperation("会员注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult register(@RequestParam String username,
                                 @RequestParam String password) {
        usersService.register(username, password);
        return CommonResult.success(null, "注册成功");
    }

    @ApiOperation("会员登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password) {
        String token = usersService.login(username, password);
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("获取会员信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult info(Principal principal) {
        if (principal == null) {
            return CommonResult.unauthorized(null);
        }
        Users users = usersService.getCurrentUser();
        return CommonResult.success(users);
    }

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAuthCode(@RequestParam String telephone) {

        String authCode = usersService.generateAuthCode(telephone);
        Map obj = new HashMap(2);
        obj.put("telephone", telephone);
        obj.put("code", authCode);
        //队列发送验证码
        telephoneMessageSender.sendMessage(obj);
        return CommonResult.success(authCode, "获取验证码成功");
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@RequestParam String telephone,
                                       @RequestParam String password,
                                       @RequestParam String authCode) {
        usersService.updatePassword(telephone, password, authCode);
        return CommonResult.success(null, "密码修改成功");
    }

    @ApiOperation("发送邮件")
    @RequestMapping(value = "/sentMail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult sentMail(@RequestParam String mail) {

        boolean isEmail = Validator.isEmail(mail);
        if (!isEmail) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "邮箱验证失败");
        }
        //构造验证码 缓存redis 并返回
        String code = usersService.generateMailCode(mail);

        Map obj = new HashMap(3);
        obj.put("mail", mail);
        obj.put("type", 1);
        obj.put("code", code);
        //加入邮件队列
        mailMessageSender.sendMessage(obj);

        return CommonResult.success("");
        // System.out.println(isEmail);
    }

    @ApiOperation("绑定邮箱")
    @RequestMapping(value = "/verifyMail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult verifyMail(@RequestParam String mail, @RequestParam String code) {

        boolean isEmail = Validator.isEmail(mail);
        if (!isEmail) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "邮箱格式不正确");
        }


        //验证验证码

        if (!userCacheService.getMailCode(mail).equals(code)) {

            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "验证码不正确");
        }
        //验证成功操作


        return CommonResult.success(code);
        // System.out.println(isEmail);
    }


}

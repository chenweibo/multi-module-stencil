package com.chen.stencil.user.service;

import com.chen.stencil.mbg.model.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author chen
 * @since 2020-07-11
 */
public interface IUsersService extends IService<Users> {


    /**
     * 根据用户名获用户
     */
    Users getUserByUsername(String username);


    /**
     * 用户注册
     */
    @Transactional
    void register(String username, String password);


    /**
     * 获取管理员
     */
    UserDetails loadUserByUsername(String username);


    /**
     * 登录后获取token
     */
    String login(String username, String password);

    /**
     * 刷新token
     */
    String refreshToken(String token);


    /**
     * 修改密码
     */
    @Transactional
    void updatePassword(String telephone, String password, String authCode);

    /**
     * 生成验证码
     */
    String generateAuthCode(String telephone);

    /**
     * 生成绑定邮箱验证码
     */
    String generateMailCode(String mail);

    /**
     * 获取当前登录管理员
     */
    Users getCurrentUser();

}

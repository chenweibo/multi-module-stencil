package com.chen.stencil.service;

import com.chen.stencil.mbg.model.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author chen
 * @since 2020-07-09
 */
public interface IAdminService extends IService<Admin> {


    /**
     * 根据用户名获取后台用户
     */
    Admin getAdminByUsername(String username);



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
     * 获取当前登录管理员
     */
    Admin getCurrentAdmin();
}

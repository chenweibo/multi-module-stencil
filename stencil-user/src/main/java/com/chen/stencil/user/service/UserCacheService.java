package com.chen.stencil.user.service;


import com.chen.stencil.mbg.model.Users;

/**
 * 会员信息缓存业务类
 * Created by macro on 2020/3/14.
 */
public interface UserCacheService {
    /**
     * 删除会员用户缓存
     */
    void delUser(Long memberId);

    /**
     * 获取会员用户缓存
     */
    Users getUser(String username);

    /**
     * 设置会员用户缓存
     */
    void setUser(Users user);

    /**
     * 设置验证码
     */
    void setAuthCode(String telephone, String authCode);

    /**
     * 获取验证码
     */
    String getAuthCode(String telephone);
}

package com.chen.stencil.service;


import com.chen.stencil.mbg.model.Admin;

/**
 * 会员信息缓存业务类
 * Created by macro on 2020/3/14.
 */
public interface AdminCacheService {
    /**
     * 删除会员用户缓存
     */
    void delAdmin(Long memberId);

    /**
     * 获取会员用户缓存
     */
    Admin getAdmin(String username);

    /**
     * 设置会员用户缓存
     */
    void setAdmin(Admin admin);

    /**
     * 设置验证码
     */
    void setAuthCode(String telephone, String authCode);

    /**
     * 获取验证码
     */
    String getAuthCode(String telephone);
}

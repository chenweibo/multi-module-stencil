package com.chen.stencil.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chen.stencil.common.exception.Asserts;
import com.chen.stencil.mbg.model.*;
import com.chen.stencil.mbg.mapper.AdminMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.stencil.security.util.JwtTokenUtil;
import com.chen.stencil.service.AdminCacheService;
import com.chen.stencil.service.IAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.chen.stencil.domain.AdminDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2020-07-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminCacheService adminCacheService;

    @Override
    public Admin getAdminByUsername(String username) {

        Admin cacheAdmin = adminCacheService.getAdmin(username);

        if (cacheAdmin != null) {
            return cacheAdmin;
        }

        Admin admin = adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, username));
        if (admin != null) {
            adminCacheService.setAdmin(admin);
            return admin;
        }

        return null;

    }

    @Override
    public String generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        adminCacheService.setAuthCode(telephone, sb.toString());
        return sb.toString();
    }


    @Override
    public void updatePassword(String telephone, String password, String authCode) {

    }


    @Override
    public void register(String username, String password) {


        //查询是否已有该用户
        Admin admin = adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, username));
        if (admin != null) {
            Asserts.fail("该用户已经存在");
        }
        //创建用户
        Admin createAdmin = new Admin();

        createAdmin.setUsername(username);

        createAdmin.setPassword(passwordEncoder.encode(password));
        createAdmin.setCreateTime(LocalDateTime.now());

        createAdmin.setStatus(1);
        adminMapper.insert(createAdmin);


    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        //获取用户信息
        Admin admin = getAdminByUsername(username);
        if (admin != null) {

            return new AdminDetails(admin);
        }

        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public Admin getCurrentAdmin() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        AdminDetails adminDetails = (AdminDetails) auth.getPrincipal();
        return adminDetails.getAdmin();
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = adminCacheService.getAuthCode(telephone);
        return authCode.equals(realAuthCode);
    }

    @Override
    public String refreshToken(String token) {
        return jwtTokenUtil.refreshHeadToken(token);
    }

}

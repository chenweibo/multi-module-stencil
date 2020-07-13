package com.chen.stencil.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chen.stencil.common.exception.Asserts;
import com.chen.stencil.mbg.model.Users;
import com.chen.stencil.mbg.mapper.UsersMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.stencil.security.util.JwtTokenUtil;
import com.chen.stencil.user.domain.UserDetails;
import com.chen.stencil.user.service.IUsersService;
import com.chen.stencil.user.service.UserCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2020-07-11
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersServiceImpl.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Override
    public Users getUserByUsername(String username) {

        Users cacheUser = userCacheService.getUser(username);

        if (cacheUser != null) {
            return cacheUser;
        }

        Users users = usersMapper.selectOne(Wrappers.<Users>lambdaQuery().eq(Users::getUsername, username));
        if (users != null) {
            userCacheService.setUser(users);
            return users;
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
        userCacheService.setAuthCode(telephone, sb.toString());
        return sb.toString();
    }


    @Override
    public String generateMailCode(String mail) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        userCacheService.setMailCode(mail, code.toString());
        return code.toString();
    }


    @Override
    public void updatePassword(String telephone, String password, String authCode) {

    }


    @Override
    public void register(String username, String password) {


        //查询是否已有该用户
        Users users = usersMapper.selectOne(Wrappers.<Users>lambdaQuery().eq(Users::getUsername, username));
        if (users != null) {
            Asserts.fail("该用户已经存在");
        }
        //创建用户
        Users createUser = new Users();

        createUser.setUsername(username);

        createUser.setPassword(passwordEncoder.encode(password));
        createUser.setCreateTime(LocalDateTime.now());

        createUser.setStatus(1);
        usersMapper.insert(createUser);


    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        //获取用户信息
        Users users = getUserByUsername(username);
        if (users != null) {

            return new UserDetails(users);
        }

        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public Users getCurrentUser() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userDetails.getUsers();
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
        String realAuthCode = userCacheService.getAuthCode(telephone);
        return authCode.equals(realAuthCode);
    }

    @Override
    public String refreshToken(String token) {
        return jwtTokenUtil.refreshHeadToken(token);
    }

}

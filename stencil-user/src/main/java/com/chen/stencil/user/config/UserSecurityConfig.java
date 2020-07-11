package com.chen.stencil.user.config;


import com.chen.stencil.security.config.SecurityConfig;
import com.chen.stencil.user.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserSecurityConfig extends SecurityConfig {

    @Autowired(required = true)
    private IUsersService usersService;

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> usersService.loadUserByUsername(username);
    }
}

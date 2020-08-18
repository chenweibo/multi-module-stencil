package com.chen.stencil.service.impl;


import com.chen.stencil.common.service.RedisService;
import com.chen.stencil.mbg.mapper.AdminMapper;
import com.chen.stencil.mbg.model.Admin;
import com.chen.stencil.security.annotation.CacheException;
import com.chen.stencil.service.AdminCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AdminCacheService实现类
 * Created by macro on 2020/3/14.
 */
@Service
public class AdminCacheServiceImpl implements AdminCacheService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AdminMapper adminMapper;

   // @Value("${redis.database}")
    private String REDIS_DATABASE="admin";
    //@Value("${redis.expire.common}")
    private Long REDIS_EXPIRE = 86400L;
   // @Value("${redis.expire.authCode}")
    private Long REDIS_EXPIRE_AUTH_CODE=90L;
   // @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN="adminLogin";
   // @Value("${redis.key.authCode}")
    private String REDIS_KEY_AUTH_CODE="ums:authCode";

    @Override
    public void delAdmin(Long adminId) {
        Admin admin = adminMapper.selectById(adminId);
        if (admin != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public Admin getAdmin(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (Admin) redisService.get(key);
    }


    @Override
    public void setAdmin(Admin member) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + member.getUsername();
        redisService.set(key, member, REDIS_EXPIRE);
    }

    @CacheException
    @Override
    public void setAuthCode(String telephone, String authCode) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + telephone;
        redisService.set(key, authCode, REDIS_EXPIRE_AUTH_CODE);
    }

    @CacheException
    @Override
    public String getAuthCode(String telephone) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + telephone;
        return (String) redisService.get(key);
    }
}

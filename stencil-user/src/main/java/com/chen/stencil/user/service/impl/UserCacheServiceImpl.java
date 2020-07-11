package com.chen.stencil.user.service.impl;


import com.chen.stencil.mbg.mapper.UsersMapper;
import com.chen.stencil.mbg.model.Users;
import com.chen.stencil.security.annotation.CacheException;
import com.chen.stencil.security.service.RedisService;
import com.chen.stencil.user.service.UserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserCacheService实现类
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {

    @Autowired(required=true)
    private RedisService redisService;

    @Autowired
    private UsersMapper usersMapper;

    // @Value("${redis.database}")
    private String REDIS_DATABASE = "user";
    //@Value("${redis.expire.common}")
    private Long REDIS_EXPIRE = 86400L;
    // @Value("${redis.expire.authCode}")
    private Long REDIS_EXPIRE_AUTH_CODE = 90L;
    // @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN = "userLogin";
    // @Value("${redis.key.authCode}")
    private String REDIS_KEY_AUTH_CODE = "user:authCode";

    @Override
    public void delUser(Long userId) {
        Users user = usersMapper.selectById(userId);
        if (user != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + user.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public Users getUser(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (Users) redisService.get(key);
    }


    @Override
    public void setUser(Users member) {
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
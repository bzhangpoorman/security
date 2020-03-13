package com.example.security.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.security.properties.BrowserProperties;
import com.example.security.properties.SecurityProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @CreateTime 2020/3/12
 * @Autor bzhang
 **/
@Component
public class RedisPersistentTokenRepository implements PersistentTokenRepository {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public synchronized void createNewToken(PersistentRememberMeToken token) {
        System.out.println("token存入redis");
        PersistentRememberMeToken tokenForSeries = getTokenForSeries(token.getSeries());
        if (tokenForSeries != null) {
            throw new DataIntegrityViolationException("Series Id '" + token.getSeries()
                    + "' already exists!");
        }

        stringRedisTemplate.
                opsForValue().
                set(token.getSeries(),
                        JSON.toJSONString(token),
                        securityProperties.getBrowser().getRememberMeSeconds() ,
                        TimeUnit.SECONDS);
        stringRedisTemplate.
                opsForValue().
                set(token.getUsername(),
                        token.getSeries(),
                        securityProperties.getBrowser().getRememberMeSeconds() ,
                        TimeUnit.SECONDS);
    }

    @Override
    public synchronized void updateToken(String series, String tokenValue, Date lastUsed) {
        System.out.println("token更新redis");
        PersistentRememberMeToken token = getTokenForSeries(series);

        PersistentRememberMeToken newToken = new PersistentRememberMeToken(
                token.getUsername(), series, tokenValue, new Date());

        stringRedisTemplate.
                opsForValue().
                set(token.getSeries(),
                        JSON.toJSONString(newToken),
                        securityProperties.getBrowser().getRememberMeSeconds() ,
                        TimeUnit.SECONDS);
        stringRedisTemplate.
                opsForValue().
                set(token.getUsername(),
                        token.getSeries(),
                        securityProperties.getBrowser().getRememberMeSeconds() ,
                        TimeUnit.SECONDS);
    }

    @Override
    public synchronized PersistentRememberMeToken getTokenForSeries(String seriesId) {
        System.out.println("token获取redis");
        String val = stringRedisTemplate.opsForValue().get(seriesId);
        if (StringUtils.isNoneBlank(val)){
            return JSON.parseObject(val,PersistentRememberMeToken.class);
        }
        return null;
    }

    @Override
    public synchronized void removeUserTokens(String username) {
        System.out.println("token删除redis");
        String series = stringRedisTemplate.opsForValue().get(username);
        if (StringUtils.isNoneBlank(series)){
            stringRedisTemplate.delete(series);
            stringRedisTemplate.delete(username);
        }

    }
}

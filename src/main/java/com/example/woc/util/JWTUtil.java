package com.example.woc.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.woc.constant.RedisKeyConst;
import com.example.woc.entity.User;
import com.example.woc.enums.ErrorEnum;
import com.example.woc.exception.LocalRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author xun
 * @create 2023/1/3 13:41
 */
@Slf4j
@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    private final RedisUtil redisUtil;

    public JWTUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public String generateToken(User user) {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.HOUR, expiration);
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("password", user.getPassword());
        builder.withClaim("id", user.getId());
        builder.withExpiresAt(time.getTime());
        return builder.sign(Algorithm.HMAC256(secret));
    }

    public Map<String, Claim> getClaims(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaims();
        } catch (JWTVerificationException e) {
            log.info("Token验证错误：{}", e.getMessage());
            throw new LocalRuntimeException(ErrorEnum.TOKEN_ERROR);
        }
    }

    public String getPassword(String token) {
        Map<String, Claim> claims = this.getClaims(token);
        return claims.get("password").asString();
    }

    public Integer getId(String token) {
        Map<String, Claim> claims = this.getClaims(token);
        return claims.get("id").asInt();
    }

    public Boolean isExpired(Integer id) {
        Long expire = redisUtil.getExpire(RedisKeyConst.getTokenKey(id));
        return expire <= 0;
    }

    public void reFreshToken(Integer id) {
        redisUtil.expire(RedisKeyConst.getTokenKey(id), 1, TimeUnit.HOURS);
    }
}

package com.dzenthai.auth.service.token;

import com.dzenthai.auth.model.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;


@Service
public class RefreshTokenService {

    private static final Long TTL = 86400L;

    private final RedisTemplate<String, String> redisTemplate;

    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(User user) {
        var username = user.getUsername();
        var token = generateCode();

        redisTemplate.opsForValue().set(token, username, TTL, TimeUnit.SECONDS);

        redisTemplate.opsForSet().add(username, token);
        redisTemplate.expire(username, TTL, TimeUnit.SECONDS);

        return token;
    }

    public String extractUsernameFromToken(String refreshToken) {
        return redisTemplate.opsForValue()
                .get(refreshToken);
    }

    public void revoke(String token) {
        String username = redisTemplate.opsForValue().get(token);
        if (username == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        redisTemplate.delete(token);
        redisTemplate.opsForSet().remove(username, token);
    }

    public void revokeAll(String refreshToken) {
        String username = redisTemplate.opsForValue().get(refreshToken);
        if (username == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        var tokens = redisTemplate.opsForSet().members(username);

        if (tokens != null) {
            redisTemplate.delete(tokens);
        }
        redisTemplate.delete(username);
    }


    private String generateCode() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }
}
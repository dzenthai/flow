package com.dzenthai.auth.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dzenthai.auth.model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AccessTokenService {

    private final Algorithm algorithm;

    private final Long accessLifetime;

    public AccessTokenService(
            @Value("${spring.jwt.access.secret}")
            String accessSecret,
            @Value("${spring.jwt.access.lifetime}")
            Long accessLifetime
    ) {
        this.algorithm = Algorithm.HMAC256(accessSecret);
        this.accessLifetime = accessLifetime;
    }

    public String generateToken(User user) {
        var jwtId = UUID.randomUUID().toString();
        var username = user.getUsername();
        var roles = user.getRole()
                .toString()
                .replace("ROLE_", "");
        var issued = Instant.now();
        var expires = issued.plusSeconds(accessLifetime);
        return JWT.create()
                .withJWTId(jwtId)
                .withSubject(username)
                .withClaim("roles", roles)
                .withClaim("user_id", user.getId())
                .withIssuedAt(issued)
                .withExpiresAt(expires)
                .sign(algorithm);
    }
}

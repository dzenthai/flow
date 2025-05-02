package com.dzenthai.auth.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dzenthai.auth.model.dto.RefreshRequest;
import com.dzenthai.auth.model.dto.RefreshResponse;
import com.dzenthai.auth.model.dto.TokenRequest;
import com.dzenthai.auth.model.dto.TokenResponse;
import com.dzenthai.auth.model.entity.User;
import com.dzenthai.auth.service.token.AccessTokenService;
import com.dzenthai.auth.service.token.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class AuthService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AccessTokenService accessTokenService;

    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserService userService,
            PasswordEncoder passwordEncoder,
            AccessTokenService accessTokenService,
            RefreshTokenService refreshTokenService
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional(readOnly = true)
    public TokenResponse token(TokenRequest tokenRequest) {
        var username = tokenRequest.username();
        log.debug("AuthService | Attempting to login user, username: {}", username);
        User user = (User) userService.loadUserByUsername(username);
        if (!user.isEnabled()) {
            throw new DisabledException("Email is not verified");
        }
        if (!passwordEncoder.matches(tokenRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return TokenResponse.builder()
                .accessToken(accessTokenService.generateToken(user))
                .refreshToken(refreshTokenService.generateToken(user))
                .build();
    }

    @Transactional(readOnly = true)
    public RefreshResponse refresh(RefreshRequest refreshRequest) {
        var token = refreshRequest.token();
        var username = refreshTokenService.extractUsernameFromToken(token);
        if (username == null) {
            throw new JWTVerificationException("Invalid refresh token");
        }
        refreshTokenService.revoke(token);
        User user = (User) userService.loadUserByUsername(username);
        return RefreshResponse.builder()
                .accessToken(accessTokenService.generateToken(user))
                .refreshToken(refreshTokenService.generateToken(user))
                .build();
    }

    public void revoke(String token) {
        refreshTokenService.revoke(token);
    }

    public void revokeAll(String token) {
        refreshTokenService.revokeAll(token);
    }
}

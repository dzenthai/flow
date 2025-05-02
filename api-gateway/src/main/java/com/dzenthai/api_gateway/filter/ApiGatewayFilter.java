package com.dzenthai.api_gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class ApiGatewayFilter extends AbstractGatewayFilterFactory<ApiGatewayFilter.Config> {

    private final Algorithm algorithm;

    public ApiGatewayFilter(@Value("${spring.jwt.access.secret}") String secret) {
        super(Config.class);
        algorithm = Algorithm.HMAC256(secret);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var method = exchange.getRequest().getMethod();
            var path = exchange.getRequest().getURI();
            log.info("ApiGatewayFilter | Incoming request. Method: {}, Path: {}",
                    method, path);

            if (path.getRawPath().startsWith("/auth/")) {
                log.info("Skipping JWT filter for /auth endpoints");
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String token;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                try {
                    var username = extractUsernameFromToken(token);
                    var roles = extractRolesFromToken(token);
                    var userId = extractUserIdFromToken(token);
                    log.info("ApiGatewayFilter | Username: {}, Roles: {}, ID: {}", username, roles, userId);

                    exchange.getAttributes().put("userId", userId);
                    exchange.getAttributes().put("username", username);

                    if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = User.builder()
                                .username(username)
                                .password("")
                                .roles(roles)
                                .build();
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        context.setAuthentication(authToken);
                        SecurityContextHolder.setContext(context);
                        log.info("ApiGatewayFilter | Authenticated user: {}", username);
                    }

                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        Long userIdAttr = exchange.getAttribute("userId");
                        String usernameAttr = exchange.getAttribute("username");
                        if (userIdAttr != null && usernameAttr != null) {
                            exchange.getResponse().getHeaders().add("X-User-Id", userIdAttr.toString());
                            exchange.getResponse().getHeaders().add("X-User-Username", usernameAttr);
                        }
                    }));
                } catch (JWTDecodeException e) {
                    log.error("ApiGatewayFilter | Invalid JWT token: {}", e.getMessage());
                    return sendErrorMessage(exchange, "Invalid JWT token");
                } catch (JWTVerificationException e) {
                    log.error("ApiGatewayFilter | Expired JWT token: {}", e.getMessage());
                    return sendErrorMessage(exchange, "Expired JWT token");
                }
            } else {
                log.warn("ApiGatewayFilter | Missing or invalid Authorization header");
                return sendErrorMessage(exchange, "Missing or invalid Authorization header");
            }
        };
    }

    public String extractUsernameFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

    public Long extractUserIdFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getClaim("user_id")
                .asLong();
    }

    public String extractRolesFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getClaim("roles")
                .asString();
    }

    private Mono<Void> sendErrorMessage(ServerWebExchange exchange, String error) {
        log.error("ApiGatewayFilter | Unauthorized access: {}", error);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    static class Config {
    }
}
package com.alin.lin.security;


import com.alin.lin.until.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

/**
 * 程式說明： JwtSessionManager
 * 程式版本： 1.0
 * 建立時間：2026/10/12下午8:40
 * 開發者：linjiaying
 **/


@Service
@ConditionalOnProperty(name = "app.auth.session-mode", havingValue = "jwt")
public class JwtSessionManager implements AuthSessionManager {

    private final JwtUtil jwtUtil;
    private final Duration ttl;

    public JwtSessionManager(JwtUtil jwtUtil,
                             @Value("${app.jwt.expire-minutes}") long expireMinutes) {
        this.jwtUtil = jwtUtil;
        this.ttl = Duration.ofMinutes(expireMinutes);
    }

    @Override
    public AuthSession create(AuthenticatedUser user) {
        return new AuthSession(jwtUtil.create(user.username()), ttl);
    }

    @Override
    public Optional<AuthenticatedUser> resolve(String credential) {
        String username = jwtUtil.parseUsername(credential);
        if (username == null) {
            return Optional.empty();
        }
        // 目前只有寫死的 admin，先重建固定資料
        return Optional.of(new AuthenticatedUser(1L, username, Set.of("USER")));
    }

    @Override
    public void invalidate(String credential) {
        // Stateless JWT 無法立即撤銷，登出只靠清除瀏覽器 Cookie
    }
}
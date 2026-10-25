package com.alin.lin.configure;

import com.alin.lin.until.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 程式說明： JwtCookieFilter
 * 程式版本： 1.0
 * 建立時間：2026/10/12下午8:17
 * 開發者：linjiaying
 **/
@Component
public class JwtCookieFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtCookieFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("token".equals(c.getName())) {
                    String username = jwtUtil.parseUsername(c.getValue());
                    if (username != null) {
                        var auth = new UsernamePasswordAuthenticationToken(
                                username, null, List.of());   // principal 就是 String username
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        }
        chain.doFilter(request, response);   // 沒登入也放行，由 SecurityConfig 決定要不要擋
    }
}


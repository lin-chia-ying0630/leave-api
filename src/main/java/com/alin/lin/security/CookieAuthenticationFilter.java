package com.alin.lin.security;

import com.alin.lin.service.AuthCookieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * 程式說明： CookieAuthenticationFilter
 * 程式版本： 1.0
 * 建立時間：2026/10/12上午11:25
 * 開發者：linjiaying
 **/


public class CookieAuthenticationFilter extends OncePerRequestFilter {

    private final AuthCookieService authCookieService;
    private final AuthSessionManager authSessionManager;

    public CookieAuthenticationFilter(
            AuthCookieService authCookieService,
            AuthSessionManager authSessionManager
    ) {
        this.authCookieService = authCookieService;
        this.authSessionManager = authSessionManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticate(request);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request) {
        Optional<String> credential = authCookieService.read(request);

        if (credential.isEmpty()) {
            return;
        }

        authSessionManager.resolve(credential.get())
                .ifPresent(this::setAuthentication);
    }

    private void setAuthentication(AuthenticatedUser user) {
        var authorities = user.roles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        var authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        user,
                        null,
                        authorities
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}

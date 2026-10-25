package com.alin.lin.service;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 程式說明： AuditorAwareImpl
 * 程式版本： 1.0
 * 建立時間：2026/9/28上午9:43
 * 開發者：linjiaying
 **/
@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !a.isAuthenticated() || a instanceof AnonymousAuthenticationToken) {
            return Optional.of("system");
        }
        return Optional.ofNullable(a.getName());
    }
}

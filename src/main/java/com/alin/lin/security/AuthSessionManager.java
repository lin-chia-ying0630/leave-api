package com.alin.lin.security;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 程式說明：
 * 程式版本： 1.0
 * 建立時間：2026/10/12上午11:16
 * 開發者：linjiaying
 **/

@Component
public interface AuthSessionManager {

    AuthSession create(AuthenticatedUser user);

    Optional<AuthenticatedUser> resolve(String credential);

    void invalidate(String credential);

    /** 讓某位使用者的所有 session 失效（停用帳號、改密碼、改權限時呼叫） */
    void invalidateAllByUser(Long userId);
}

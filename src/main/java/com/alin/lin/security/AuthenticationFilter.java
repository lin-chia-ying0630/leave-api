package com.alin.lin.security;

/**
 * 程式說明： AuthenticationFilter
 * 程式版本： 1.0
 * 建立時間：2026/10/12上午11:14
 * 開發者：linjiaying
 **/
public class AuthenticationFilter {
    /**
     *        └─ AuthSessionManager     # JWT 或 Redis 的可替換策略
     *             ├─ JwtSessionManager
     *             └─ RedisSessionManager
        */
    private AuthSessionManager authSessionManager;
    private final String JwtSessionManager = "JwtSessionManager";

}

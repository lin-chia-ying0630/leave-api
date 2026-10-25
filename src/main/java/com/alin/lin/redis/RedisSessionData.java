package com.alin.lin.redis;

/**
 * 程式說明： RedisSessionData
 * 程式版本： 1.0
 * 建立時間：2026/10/12上午11:33
 * 開發者：linjiaying
 **/
public record RedisSessionData(
        Long userId,
        String username,
        java.util.Set<String> roles
) {
}

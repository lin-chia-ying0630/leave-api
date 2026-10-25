package com.alin.lin.security;

import java.time.Duration;

/**
 * 程式說明： AuthSession
 * 程式版本： 1.0
 * 建立時間：2026/10/12上午11:23
 * 開發者：linjiaying
 **/
public record AuthSession(
        String credential,
        Duration ttl
) {
}
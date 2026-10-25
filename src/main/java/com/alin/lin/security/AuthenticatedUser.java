package com.alin.lin.security;

import java.util.Set;

/**
 * 程式說明： AuthenticatedUser
 * 程式版本： 1.0
 * 建立時間：2026/10/12上午11:21
 * 開發者：linjiaying
 **/
public record AuthenticatedUser(
        Long id,
        String username,
        Set<String> roles
) {
}
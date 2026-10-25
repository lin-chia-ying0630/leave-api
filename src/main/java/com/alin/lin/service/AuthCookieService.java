package com.alin.lin.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

/**
 * 程式說明：
 * 程式版本： 1.0
 * 建立時間：2026/10/12上午9:21
 * 開發者：linjiaying
 * 建立與清除 Cookie
 **/
@Service
public class AuthCookieService {

    private static final String COOKIE_NAME = "AUTH_SESSION";

    public Optional<String> read(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> !value.isBlank())
                .findFirst();
    }

    public ResponseCookie create(String credential, Duration ttl) {
        // credential 可以是 JWT 或 Redis Session ID
        return ResponseCookie.from("AUTH_SESSION", credential)
                .httpOnly(true)
                .secure(false) // localhost；正式環境應為 true.secure(false) // localhost；正式環境應為 true
                .sameSite("Lax") // SameSite=Strict
                .path("/") // Path=/
                .maxAge(ttl)
                .build();
    }

    public ResponseCookie clear() {
        // 同名、同 Path，Max-Age=0
        return ResponseCookie.from("AUTH_SESSION", "")
                .httpOnly(true)
                .secure(false)
                 //.sameSite("Strict")
                .sameSite("Lax")   // 跟 create() 一致
                .path("/")         // ← 必須跟建立時相同才刪得掉
                .maxAge(Duration.ZERO)
                .build();
    }
}

package com.alin.lin.security;

/**
 * 程式說明：
 * 程式版本： 1.0
 * 建立時間：2026/10/6下午8:23
 * 開發者：linjiaying
 **/
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}

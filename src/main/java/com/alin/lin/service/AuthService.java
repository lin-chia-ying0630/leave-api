package com.alin.lin.service;

import com.alin.lin.request.LoginRequest;
import com.alin.lin.response.LoginResponse;
import com.alin.lin.security.AuthenticatedUser;

/**
 * 程式說明：
 * 程式版本： 1.0
 * 建立時間：2026/10/6下午7:55
 * 開發者：linjiaying
 **/
public interface AuthService {
    AuthenticatedUser login(LoginRequest loginRequest);
}

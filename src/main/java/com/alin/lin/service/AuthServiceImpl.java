package com.alin.lin.service;

import com.alin.lin.request.LoginRequest;
import com.alin.lin.response.LoginResponse;
import com.alin.lin.security.AuthenticatedUser;
import com.alin.lin.security.AuthenticationFailedException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 程式說明： AuthServiceImpl
 * 程式版本： 1.0
 * 建立時間：2026/10/6下午8:00
 * 開發者：linjiaying
 **/
@Service
public class AuthServiceImpl implements AuthService {
    // 1. 用 appUserRepository.findByUsername 找使用者
    //    找不到 → throw new AuthenticationFailedException("帳號或密碼錯誤")
    // 2. 用 passwordEncoder.matches(明碼, 雜湊) 比對
    //    不符 → 同樣丟「帳號或密碼錯誤」（為什麼不分開說「帳號不存在」？寫進筆記）
    // 3. 回傳 new AuthenticatedUser(user.getId(), user.getUsername(), Set.of(user.getRole()))
    @Override
    public AuthenticatedUser login(LoginRequest loginRequest) {
        // 這裡可以添加實際的驗證邏輯，例如檢查用戶名和密碼是否正確
        // 目前僅返回一個模擬的 token 和用戶名
        if ("admin".equals(loginRequest.username()) && "1234".equals(loginRequest.password())) {
            return new AuthenticatedUser(1L, loginRequest.username(), Set.of("USER"));
        }
        throw new AuthenticationFailedException("帳號或密碼錯誤");
    }
}

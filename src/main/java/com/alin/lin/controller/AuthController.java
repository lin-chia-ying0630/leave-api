package com.alin.lin.controller;


import com.alin.lin.request.LoginRequest;
import com.alin.lin.response.ApiResponse;
import com.alin.lin.response.LoginResponse;
import com.alin.lin.response.MeResponse;
import com.alin.lin.security.AuthSession;
import com.alin.lin.security.AuthSessionManager;
import com.alin.lin.security.AuthenticatedUser;
import com.alin.lin.service.AuthCookieService;
import com.alin.lin.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 程式說明： AuthController
 * 程式版本： 1.0
 * 建立時間：2026/10/6下午7:49
 * 開發者：linjiaying
 **/
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthSessionManager authSessionManager;
    private final AuthCookieService authCookieService;
    public AuthController(AuthService authService,AuthSessionManager authSessionManager,AuthCookieService authCookieService) {
        this.authService = authService;
        this.authSessionManager = authSessionManager;
        this.authCookieService = authCookieService;
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest req) {
        // 目前仍是寫死帳密，之後換成查 DB
//        if (!"admin".equals(req.username()) || !"1234".equals(req.password())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(ApiResponse.failure("AUTH_FAILED", "帳號或密碼錯誤"));
//        }
        AuthenticatedUser user = authService.login(req);
        AuthSession session = authSessionManager.create(user);

        ResponseCookie cookie = authCookieService.create(
                session.credential(),
                session.ttl());


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(new LoginResponse(true, "登入成功")));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<ApiResponse<Void>> logout() {
//        ResponseCookie cookie = ResponseCookie.from("token", "")
//                .httpOnly(true)
//                .secure(false)
//                .sameSite("Lax")
//                .path("/")
//                .maxAge(0)
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body(ApiResponse.success(null));
//    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        // 先讓後端的 session 失效（Redis 模式會刪 key；JWT 模式什麼都不做）
        authCookieService.read(request).ifPresent(authSessionManager::invalidate);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookieService.clear().toString())
                .body(ApiResponse.success(null));
    }
    /**從認證上下文取得目前使用者。
     *return MeResponse
     * */

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponse>> me(Authentication authentication) {
        AuthenticatedUser user =
                (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(
                ApiResponse.success(new MeResponse(user.username())));
    }
}
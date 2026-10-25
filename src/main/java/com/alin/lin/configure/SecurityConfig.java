package com.alin.lin.configure;


import com.alin.lin.security.AuthSessionManager;
import com.alin.lin.security.CookieAuthenticationFilter;
import com.alin.lin.service.AuthCookieService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 程式說明： SecurityFilterChain
 * 程式版本： 1.0
 * 建立時間：2026/9/28上午9:49
 * 開發者：linjiaying
 **/
@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CookieAuthenticationFilter cookieAuthenticationFilter(
            AuthCookieService authCookieService,
            AuthSessionManager authSessionManager
    ) {
        return new CookieAuthenticationFilter(
                authCookieService,
                authSessionManager
        );
    }

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            CookieAuthenticationFilter authenticationFilter
    ) throws Exception {

        http
                // 學習階段先關閉；Cookie 設了 SameSite=Lax，上線前要再評估 CSRF
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login",
                                "/api/auth/logout"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/auth/me"
                        ).authenticated()
                        .requestMatchers("/api/leaves/**")
                        .authenticated()
                        .anyRequest()
                        .denyAll()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write(
                            "{\"data\":null,\"code\":\"UNAUTHORIZED\",\"message\":\"尚未登入或登入已過期\"}");
                }))
                .addFilterBefore(
                        authenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
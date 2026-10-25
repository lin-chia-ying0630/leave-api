package com.alin.lin.response;

/**
 * 程式說明：
 * 程式版本： 1.0
 * 建立時間：2026/9/27上午8:59
 * 開發者：linjiaying
 **/
public record ApiResponse<T>(T data, String code, String message) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, null, null);
    }

    public static <T> ApiResponse<T> failure(String code, String message) {
        return new ApiResponse<>(null, code, message);
    }
}
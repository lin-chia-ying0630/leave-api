package com.alin.lin.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 程式說明：
 * 程式版本： 1.0
 * 建立時間：2026/10/6下午7:54
 * 開發者：linjiaying
 **/
public record LoginResponse(boolean success, String message) {}

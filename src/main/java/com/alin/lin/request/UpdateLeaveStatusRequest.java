package com.alin.lin.request;

import jakarta.validation.constraints.NotNull;

/**
 * 狀態更新的 API 輸入，只允許後端定義的封閉狀態值。
 */
public record UpdateLeaveStatusRequest(@NotNull LeaveStatus status) {
}

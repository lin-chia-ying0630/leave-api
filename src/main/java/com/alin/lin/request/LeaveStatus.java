package com.alin.lin.request;

import jakarta.persistence.Enumerated;
import lombok.Getter;

/**
 * 程式說明：
 * 程式版本： 1.0
 * 建立時間：2026/9/25下午3:25
 * 開發者：linjiaying
 **/
@Getter
public enum LeaveStatus {
    //（待審/核准/駁回）
    PENDING("待審","P"),
    APPROVED("核准","A"),
    REJECTED("駁回","R");
    private final String displayName;
    private final String code;

    LeaveStatus(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

}

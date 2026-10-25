package com.alin.lin.until;

public class LeaveNotFoundException extends RuntimeException {
    public LeaveNotFoundException(Long id) {
        super("找不到請假單：" + id);
    }
}

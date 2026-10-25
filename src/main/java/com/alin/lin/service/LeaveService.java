package com.alin.lin.service;

import com.alin.lin.request.LeaveRequest;
import com.alin.lin.request.LeaveStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 程式說明：
 * 程式版本： 1.0
 * 建立時間：2026/9/27上午9:12
 * 開發者：linjiaying
 **/

public interface LeaveService {
    LeaveRequest findById(Long id);

    LeaveRequest save(LeaveRequest leaveRequest);

    LeaveRequest updateStatus(Long id, LeaveStatus status);

    void deleteById(Long id);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findAll();
}

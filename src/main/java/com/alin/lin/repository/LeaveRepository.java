package com.alin.lin.repository;

import com.alin.lin.request.LeaveRequest;
import com.alin.lin.request.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 程式說明： LeaveRepository
 * 程式版本： 1.0
 * 建立時間：2026/9/25下午3:44
 * 開發者：linjiaying
 **/
@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByStatus(LeaveStatus status);
}

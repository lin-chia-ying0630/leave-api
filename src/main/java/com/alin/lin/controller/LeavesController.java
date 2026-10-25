package com.alin.lin.controller;

import com.alin.lin.request.LeaveRequest;
import com.alin.lin.request.LeaveStatus;
import com.alin.lin.request.UpdateLeaveStatusRequest;
import com.alin.lin.response.ApiResponse;
import com.alin.lin.service.LeaveService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 程式說明： Leavescontroller
 * 程式版本： 1.0
 * 建立時間：2026/9/26下午7:28
 * 開發者：linjiaying
 **/



@RestController
@RequestMapping("/api/leaves")
public class LeavesController {

    private  static final Logger log =
            LoggerFactory.getLogger(LeavesController.class);
      //* GET /api/leaves：回傳所有請假單。
    //* POST /api/leaves：新增一筆，狀態預設「待審」。
    private final LeaveService leaveService;
    public LeavesController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // 查詢（推薦）
    @GetMapping("/{id}")
    public ApiResponse<LeaveRequest> getById(@PathVariable Long id) {
        log.info("收到請假單查詢請求，id={}", id);
        LeaveRequest r = leaveService.findById(id);
        log.info("請假單查詢完成，id={}", id);
        return ApiResponse.success(r);
    }

    // 建立
    @PostMapping
    public ApiResponse<LeaveRequest> createLeaveRequest(@Valid @RequestBody LeaveRequest leaveRequest) {
        log.info(
                "收到新增請假單請求，leaveType={}，startDate={}，endDate={}",
                leaveRequest.getLeaveType(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate()
        );
        LeaveRequest saved = leaveService.save(leaveRequest);
        log.info("請假單新增完成，id={}，status={}",
                saved.getId(), saved.getStatus());
        return ApiResponse.success(saved);
    }


    //狀態與篩選 API
    //* PUT /api/leaves/{id}/status：改成核准或駁回。
    @PutMapping("/{id}/status")
    public ApiResponse<LeaveRequest> updateStatus(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateLeaveStatusRequest request) {
        LeaveRequest updated = leaveService.updateStatus(id, request.status());
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveService.deleteById(id);
        return ApiResponse.success(null);
    }
    //* GET /api/leaves?status=PENDING：依狀態篩選。
    @GetMapping
    public ApiResponse<List<LeaveRequest>> getLeavesByStatus(
            @RequestParam(required = false) LeaveStatus status) {
        if (status == null) {
            List<LeaveRequest> leaves = leaveService.findAll();
            return ApiResponse.success(leaves);
        }
        List<LeaveRequest> leaves = leaveService.findByStatus(status);
        return ApiResponse.success(leaves);
    }

}

package com.alin.lin.service;

import com.alin.lin.repository.LeaveRepository;
import com.alin.lin.request.LeaveRequest;
import com.alin.lin.request.LeaveStatus;
import com.alin.lin.until.LeaveNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 程式說明： LeaveServiceImpl
 * 程式版本： 1.0
 * 建立時間：2026/9/27上午9:23
 * 開發者：linjiaying
 **/
@Service
public class LeaveSeviceImpl implements LeaveService {
    private static final Logger log =
            LoggerFactory.getLogger(LeaveSeviceImpl.class);
    private final LeaveRepository leaveRepository;

    public LeaveSeviceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public LeaveRequest findById(Long id) {
        log.debug("開始查詢請假單，id={}", id);
        return findExisting(id);
    }

    @Override
    @Transactional
    public LeaveRequest save(LeaveRequest leaveRequest) {
        // entity @PrePersist and JPA auditing handle defaults (status, timestamps, createdBy)
        LeaveRequest saved = leaveRepository.save(leaveRequest);

        log.info("請假單已寫入資料庫，id={}，status={}",
                saved.getId(), saved.getStatus());

        return saved;
    }

    @Override
    @Transactional
    public LeaveRequest updateStatus(Long id, LeaveStatus status) {
        LeaveRequest existing = findExisting(id);
        existing.setStatus(status);
        return leaveRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        LeaveRequest existing = findExisting(id);
        leaveRepository.delete(existing);
    }

    @Override
    public List<LeaveRequest> findByStatus(LeaveStatus status) {
        return leaveRepository.findByStatus(status);
    }

    @Override
    public List<LeaveRequest> findAll() {
        return leaveRepository.findAll();
    }

    private LeaveRequest findExisting(Long id) {
        return leaveRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("查無請假單，id={}", id);
                    return new LeaveNotFoundException(id);
                });
    }
}

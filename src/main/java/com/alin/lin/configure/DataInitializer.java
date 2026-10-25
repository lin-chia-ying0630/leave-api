package com.alin.lin.configure;

import com.alin.lin.repository.LeaveRepository;
import com.alin.lin.request.LeaveRequest;
import com.alin.lin.request.LeaveStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

/**
 * 程式說明： DataInitializer
 * 程式版本： 1.0
 * 建立時間：2026/9/28下午6:14
 * 開發者：linjiaying
 **/
@Configuration
@Profile({"dev", "test"})
public class DataInitializer {

    @Bean
    CommandLineRunner initData(LeaveRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.save(LeaveRequest.builder()
                    .applicant("王小明").leaveType("特休")
                    .startDate(LocalDate.parse("2026-10-01")).endDate(LocalDate.parse("2026-10-02"))
                    .reason("家庭旅遊").status(LeaveStatus.PENDING).build());
            repository.save(LeaveRequest.builder()
                    .applicant("陳小美").leaveType("病假")
                    .startDate(LocalDate.parse("2026-10-05")).endDate(LocalDate.parse("2026-10-05"))
                    .reason("感冒就醫").status(LeaveStatus.APPROVED).build());
            repository.save(LeaveRequest.builder()
                    .applicant("林大華").leaveType("事假")
                    .startDate(LocalDate.parse("2026-10-08")).endDate(LocalDate.parse("2026-10-09"))
                    .reason("辦理私事").status(LeaveStatus.REJECTED).build());
        };
    }
}

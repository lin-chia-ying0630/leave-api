package com.alin.lin.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 程式說明：
 * 程式版本： 1.0
 * 建立時間：2026/9/28下午12:44
 * 開發者：linjiaying
 **/
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LeavesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllLeavesWithCorsHeader() throws Exception {
        mockMvc.perform(get("/api/leaves")
                        .header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$.code").value((Object) null))
                .andExpect(jsonPath("$.message").value((Object) null));
    }

    @Test
    void shouldFilterLeavesByStatus() throws Exception {
        mockMvc.perform(get("/api/leaves").param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].status", everyItem(org.hamcrest.Matchers.is("PENDING"))));
    }

    @Test
    void shouldReturnLeaveById() throws Exception {
        mockMvc.perform(get("/api/leaves/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.applicant").value("王小明"));
    }

    @Test
    void shouldCreateLeaveWithPendingStatus() throws Exception {
        mockMvc.perform(post("/api/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "applicant": "測試人員",
                                  "leaveType": "特休",
                                  "startDate": "2026-10-10",
                                  "endDate": "2026-10-11",
                                  "reason": "整合測試"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    void shouldRejectCreateRequestWithoutApplicant() throws Exception {
        mockMvc.perform(post("/api/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "leaveType": "特休",
                                  "startDate": "2026-10-10",
                                  "endDate": "2026-10-11",
                                  "reason": "整合測試"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST_BODY"));
    }

    @Test
    void shouldUpdateLeaveStatusFromJsonBody() throws Exception {
        mockMvc.perform(put("/api/leaves/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"APPROVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));
    }

    @Test
    void shouldDeleteLeave() throws Exception {
        mockMvc.perform(delete("/api/leaves/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value((Object) null))
                .andExpect(jsonPath("$.code").value((Object) null));

        mockMvc.perform(get("/api/leaves/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("LEAVE_NOT_FOUND"));

        mockMvc.perform(delete("/api/leaves/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("LEAVE_NOT_FOUND"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingLeave() throws Exception {
        mockMvc.perform(put("/api/leaves/999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"APPROVED\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("LEAVE_NOT_FOUND"));
    }

    @Test
    void shouldRejectUnknownStatus() throws Exception {
        mockMvc.perform(get("/api/leaves").param("status", "UNKNOWN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAMETER"));

        mockMvc.perform(put("/api/leaves/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"UNKNOWN\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST_BODY"));
    }

}

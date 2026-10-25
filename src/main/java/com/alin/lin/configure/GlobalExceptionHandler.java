package com.alin.lin.configure;

import com.alin.lin.response.ApiResponse;
import com.alin.lin.security.AuthenticationFailedException;
import com.alin.lin.until.LeaveNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 程式說明： GlobalExceptionHandler
 * 程式版本： 1.0
 * 建立時間：2026/9/28下午12:52
 * 開發者：linjiaying
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationFailed(
            AuthenticationFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(
                        "AUTHENTICATION_FAILED",
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(LeaveNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleLeaveNotFound(LeaveNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure("LEAVE_NOT_FOUND", ex.getMessage()));
    }

    // 例如 status=ABC，無法轉成 LeaveStatus
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(
                        "INVALID_PARAMETER",
                        "參數 " + ex.getName() + " 格式錯誤"
                ));
    }

    // 例如缺少 ?status=PENDING
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParameter(
            MissingServletRequestParameterException ex) {

        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(
                        "MISSING_PARAMETER",
                        "缺少必要參數：" + ex.getParameterName()
                ));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<Void>> handleInvalidRequestBody(Exception ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(
                        "INVALID_REQUEST_BODY",
                        "請求內容格式錯誤"
                ));
    }

    // 真正沒有預期到的後端錯誤
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("API 發生未預期錯誤", ex);

        return ResponseEntity.internalServerError()
                .body(ApiResponse.failure(
                        "INTERNAL_ERROR",
                        "系統處理失敗"
                ));
    }
}

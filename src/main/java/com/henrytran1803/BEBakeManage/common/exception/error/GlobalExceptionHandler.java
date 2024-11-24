package com.henrytran1803.BEBakeManage.common.exception.error;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Tạo danh sách lỗi
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        // Tạo phản hồi với mã lỗi VALIDATION_ERROR
        return ResponseEntity.badRequest().body(
                ApiResponse.Q_failure(null, QuyExeption.INVALID_INPUT, errors.toString())
        );
    }
}

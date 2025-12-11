package com.example.demo.handle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 业务异常处理
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.put("msg", e.getMessage());
        result.put("data", null);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Redis相关异常处理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.SERVICE_UNAVAILABLE.value());
        result.put("msg", "服务异常：" + e.getMessage());
        result.put("data", null);
        return new ResponseEntity<>(result, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

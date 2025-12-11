package com.example.demo.Controller;

import com.example.demo.Service.CodeGenerateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api
@RestController
@RequestMapping("/api/code")
public class CodeGenerateController {
    @Autowired
    private CodeGenerateService codeGenerateService;


    // ---------------------- 产物编码接口 (前缀加6位流水号)----------------------
    @GetMapping("/product")
    public ResponseEntity<Map<String, Object>> getProductCode(@RequestParam String CodeKey) {
        return successResponse(codeGenerateService.generateCode(CodeKey));
    }

    // ---------------------- 任务单编码接口（前缀+日期+2位流水号） ----------------------
    @GetMapping("/task")
    public ResponseEntity<Map<String, Object>> getTaskCode(@RequestParam String CodeKey) {
        return successResponse(codeGenerateService.generateTaskCode(CodeKey));
    }

    // ---------------------- 孔号编码接口（前缀+2位流水号） ----------------------
    @GetMapping("/grid")
    public ResponseEntity<Map<String, Object>> getGridCode(@RequestParam String CodeKey) {
        return successResponse(codeGenerateService.generateGridCode(CodeKey));
    }

    @GetMapping("/one")
    public ResponseEntity<Map<String, Object>> getOneCode(@RequestParam String CodeKey) {
        return successResponse(codeGenerateService.generateOneCode(CodeKey));
    }

    @GetMapping("/three")
    public ResponseEntity<Map<String, Object>> getThreeCode(@RequestParam String CodeKey) {
        return successResponse(codeGenerateService.generateThreeCode(CodeKey));
    }

    // ---------------------- 通用响应封装 ----------------------
    private ResponseEntity<Map<String, Object>> successResponse(String code) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("msg", "success");
        result.put("data", code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

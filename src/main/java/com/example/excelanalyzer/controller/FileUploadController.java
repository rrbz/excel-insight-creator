
package com.example.excelanalyzer.controller;

import com.example.excelanalyzer.model.DataAnalysisResult;
import com.example.excelanalyzer.service.ExcelAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private ExcelAnalysisService excelAnalysisService;

    @PostMapping("/upload")
    public ResponseEntity<DataAnalysisResult> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            DataAnalysisResult result = excelAnalysisService.analyzeExcelFile(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/generate-chart")
    public ResponseEntity<Map<String, Object>> generateChart(
            @RequestBody Map<String, Object> chartRequest) {
        try {
            Map<String, Object> chartData = excelAnalysisService.generateChartData(chartRequest);
            return ResponseEntity.ok(chartData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

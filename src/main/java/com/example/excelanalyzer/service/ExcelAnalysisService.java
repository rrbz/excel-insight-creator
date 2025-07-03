
package com.example.excelanalyzer.service;

import com.example.excelanalyzer.model.ColumnStatistics;
import com.example.excelanalyzer.model.DataAnalysisResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelAnalysisService {

    public DataAnalysisResult analyzeExcelFile(MultipartFile file) throws IOException {
        Workbook workbook = null;
        try {
            // 根据文件扩展名创建相应的工作簿
            String filename = file.getOriginalFilename();
            if (filename.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (filename.endsWith(".xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else {
                throw new IllegalArgumentException("不支持的文件格式");
            }

            Sheet sheet = workbook.getSheetAt(0);
            List<Map<String, Object>> data = new ArrayList<>();
            List<String> headers = new ArrayList<>();

            // 读取表头
            Row headerRow = sheet.getRow(0);
            if (headerRow != null) {
                for (Cell cell : headerRow) {
                    headers.add(getCellValueAsString(cell));
                }
            }

            // 读取数据行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Map<String, Object> rowData = new HashMap<>();
                    boolean hasData = false;
                    
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j);
                        Object value = getCellValue(cell);
                        if (value != null && !value.toString().trim().isEmpty()) {
                            hasData = true;
                        }
                        rowData.put(headers.get(j), value);
                    }
                    
                    if (hasData) {
                        data.add(rowData);
                    }
                }
            }

            DataAnalysisResult result = new DataAnalysisResult(data, headers, filename);
            result.setStatistics(calculateStatistics(data, headers));
            return result;

        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private String getCellValueAsString(Cell cell) {
        Object value = getCellValue(cell);
        return value != null ? value.toString() : "";
    }

    private Map<String, ColumnStatistics> calculateStatistics(List<Map<String, Object>> data, List<String> headers) {
        Map<String, ColumnStatistics> statistics = new HashMap<>();

        for (String header : headers) {
            List<Object> values = data.stream()
                    .map(row -> row.get(header))
                    .filter(Objects::nonNull)
                    .filter(v -> !v.toString().trim().isEmpty())
                    .collect(Collectors.toList());

            ColumnStatistics stats = new ColumnStatistics();
            stats.setCount(values.size());

            // 判断是否为数值类型
            List<Double> numericValues = values.stream()
                    .filter(v -> isNumeric(v.toString()))
                    .map(v -> Double.parseDouble(v.toString()))
                    .collect(Collectors.toList());

            if (numericValues.size() > values.size() * 0.7) {
                // 数值类型
                stats.setType("numeric");
                if (!numericValues.isEmpty()) {
                    stats.setAverage(numericValues.stream().mapToDouble(Double::doubleValue).average().orElse(0));
                    stats.setMax(numericValues.stream().mapToDouble(Double::doubleValue).max().orElse(0));
                    stats.setMin(numericValues.stream().mapToDouble(Double::doubleValue).min().orElse(0));
                }
            } else {
                // 文本类型
                stats.setType("text");
                stats.setUniqueValues((int) values.stream().distinct().count());
            }

            statistics.put(header, stats);
        }

        return statistics;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Map<String, Object> generateChartData(Map<String, Object> chartRequest) {
        // 这里可以根据前端传来的参数生成图表数据
        // 实际实现中可以结合数据库或缓存来获取之前上传的数据
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "图表数据生成成功");
        return response;
    }
}

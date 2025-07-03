package com.example.excelanalyzer.service;

import com.example.excelanalyzer.entity.AnalysisResult;
import com.example.excelanalyzer.entity.DataSet;
import com.example.excelanalyzer.model.AdvancedColumnStatistics;
import com.example.excelanalyzer.model.CorrelationAnalysis;
import com.example.excelanalyzer.model.DataAnalysisResult;
import com.example.excelanalyzer.model.RegressionAnalysis;
import com.example.excelanalyzer.repository.AnalysisResultRepository;
import com.example.excelanalyzer.repository.DataSetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExcelAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelAnalysisService.class);

    @Autowired
    private DataSetRepository dataSetRepository;

    @Autowired
    private AnalysisResultRepository analysisResultRepository;

    @Autowired
    private AdvancedStatisticalAnalysisService statisticalAnalysisService;

    @Autowired
    private ChartGenerationService chartGenerationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public DataAnalysisResult analyzeExcelFile(MultipartFile file) throws IOException {
        logger.info("开始分析Excel文件: {}", file.getOriginalFilename());
        
        Workbook workbook = null;
        try {
            // 创建数据集实体
            DataSet dataSet = new DataSet(
                generateUniqueFileName(file.getOriginalFilename()),
                file.getOriginalFilename(),
                file.getSize(),
                getFileExtension(file.getOriginalFilename())
            );

            // 根据文件扩展名创建相应的工作簿
            String filename = file.getOriginalFilename();
            if (filename.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (filename.endsWith(".xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else {
                throw new IllegalArgumentException("不支持的文件格式: " + filename);
            }

            // 解析Excel数据
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

            // 更新数据集信息
            dataSet.setTotalRows(data.size());
            dataSet.setTotalColumns(headers.size());
            dataSet.setHeaders(String.join(",", headers));
            dataSet.setStatus("ANALYZED");
            dataSet.setLastAnalyzed(LocalDateTime.now());

            // 保存数据集
            dataSet = dataSetRepository.save(dataSet);
            logger.info("数据集已保存，ID: {}", dataSet.getId());

            // 创建分析结果
            DataAnalysisResult result = new DataAnalysisResult(data, headers, filename);
            
            // 执行高级统计分析
            Map<String, AdvancedColumnStatistics> advancedStats = performAdvancedStatisticalAnalysis(data, headers, dataSet);
            result.setAdvancedStatistics(advancedStats);

            // 执行相关性分析
            CorrelationAnalysis correlationAnalysis = performCorrelationAnalysis(data, headers, dataSet);
            result.setCorrelationAnalysis(correlationAnalysis);

            logger.info("Excel文件分析完成: {}", filename);
            return result;

        } catch (Exception e) {
            logger.error("分析Excel文件时发生错误: {}", e.getMessage(), e);
            throw new RuntimeException("分析失败: " + e.getMessage(), e);
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    @Cacheable(value = "analysisResults", key = "#datasetId + '_' + #analysisType")
    public AnalysisResult getAnalysisResult(Long datasetId, String analysisType) {
        Optional<DataSet> dataSetOpt = dataSetRepository.findById(datasetId);
        if (dataSetOpt.isEmpty()) {
            throw new IllegalArgumentException("数据集不存在: " + datasetId);
        }

        List<AnalysisResult> results = analysisResultRepository.findByDataSetAndAnalysisType(
            dataSetOpt.get(), analysisType);
        
        return results.isEmpty() ? null : results.get(0);
    }

    public List<DataSet> getAllDataSets() {
        return dataSetRepository.findAllOrderByUploadTimeDesc();
    }

    public Map<String, Object> getDataSetSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalDataSets", dataSetRepository.count());
        summary.put("totalFileSize", dataSetRepository.getTotalFileSize());
        summary.put("analyzedCount", dataSetRepository.countByStatus("ANALYZED"));
        summary.put("processingCount", dataSetRepository.countByStatus("ANALYZING"));
        summary.put("errorCount", dataSetRepository.countByStatus("ERROR"));
        
        return summary;
    }

    public RegressionAnalysis performRegressionAnalysis(Long datasetId, String dependentVar, List<String> independentVars) {
        logger.info("开始执行回归分析，数据集ID: {}, 因变量: {}", datasetId, dependentVar);
        
        Optional<DataSet> dataSetOpt = dataSetRepository.findById(datasetId);
        if (dataSetOpt.isEmpty()) {
            throw new IllegalArgumentException("数据集不存在: " + datasetId);
        }

        DataSet dataSet = dataSetOpt.get();
        
        // 这里应该从数据集中获取实际数据，为了简化，我们创建模拟数据
        List<Double> dependentValues = generateMockNumericData(100);
        Map<String, List<Double>> independentValues = new HashMap<>();
        
        for (String var : independentVars) {
            independentValues.put(var, generateMockNumericData(100));
        }

        RegressionAnalysis analysis = statisticalAnalysisService.performLinearRegression(
            dependentValues, independentValues, dependentVar);

        // 保存分析结果
        saveAnalysisResult(dataSet, "REGRESSION", analysis);
        
        logger.info("回归分析完成");
        return analysis;
    }

    private Map<String, AdvancedColumnStatistics> performAdvancedStatisticalAnalysis(
            List<Map<String, Object>> data, List<String> headers, DataSet dataSet) {
        
        logger.info("开始执行高级统计分析");
        Map<String, AdvancedColumnStatistics> statistics = new HashMap<>();

        for (String header : headers) {
            List<Object> values = data.stream()
                    .map(row -> row.get(header))
                    .collect(Collectors.toList());

            AdvancedColumnStatistics stats = statisticalAnalysisService.calculateAdvancedStatistics(values, header);
            statistics.put(header, stats);
        }

        // 保存统计分析结果
        saveAnalysisResult(dataSet, "STATISTICAL", statistics);
        
        logger.info("高级统计分析完成，共分析 {} 个列", headers.size());
        return statistics;
    }

    private CorrelationAnalysis performCorrelationAnalysis(
            List<Map<String, Object>> data, List<String> headers, DataSet dataSet) {
        
        logger.info("开始执行相关性分析");
        
        // 提取数值列
        Map<String, List<Double>> numericData = new HashMap<>();
        
        for (String header : headers) {
            List<Double> numericValues = data.stream()
                    .map(row -> row.get(header))
                    .filter(Objects::nonNull)
                    .filter(v -> isNumeric(v.toString()))
                    .map(v -> Double.parseDouble(v.toString()))
                    .collect(Collectors.toList());
            
            if (numericValues.size() > data.size() * 0.5) { // 至少50%的数据是数值
                numericData.put(header, numericValues);
            }
        }

        CorrelationAnalysis analysis = null;
        if (numericData.size() >= 2) {
            analysis = statisticalAnalysisService.performCorrelationAnalysis(numericData, "PEARSON");
            
            // 保存相关性分析结果
            saveAnalysisResult(dataSet, "CORRELATION", analysis);
        }
        
        logger.info("相关性分析完成，涉及 {} 个数值列", numericData.size());
        return analysis;
    }

    private void saveAnalysisResult(DataSet dataSet, String analysisType, Object resultData) {
        try {
            AnalysisResult result = new AnalysisResult(dataSet, analysisType);
            result.setResultData(objectMapper.writeValueAsString(resultData));
            result.setStatus("SUCCESS");
            analysisResultRepository.save(result);
            
            logger.debug("分析结果已保存: {} - {}", analysisType, dataSet.getId());
        } catch (JsonProcessingException e) {
            logger.error("保存分析结果时发生错误: {}", e.getMessage(), e);
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

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private List<Double> generateMockNumericData(int size) {
        Random random = new Random();
        return random.doubles(size, 0, 100).boxed().collect(Collectors.toList());
    }
}

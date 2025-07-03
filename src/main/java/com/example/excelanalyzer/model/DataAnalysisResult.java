
package com.example.excelanalyzer.model;

import java.util.List;
import java.util.Map;

public class DataAnalysisResult {
    private List<Map<String, Object>> data;
    private List<String> headers;
    private String fileName;
    private Map<String, ColumnStatistics> statistics; // 保留原有的简单统计
    private Map<String, AdvancedColumnStatistics> advancedStatistics; // 新增高级统计
    private CorrelationAnalysis correlationAnalysis; // 相关性分析结果
    private List<RegressionAnalysis> regressionAnalyses; // 回归分析结果
    private int totalRows;
    private int totalColumns;
    private long analysisTimestamp;
    private String dataQualityScore; // 数据质量评分
    private List<String> dataQualityIssues; // 数据质量问题
    private Map<String, Object> summaryStatistics; // 汇总统计
    
    // Constructors
    public DataAnalysisResult() {
        this.analysisTimestamp = System.currentTimeMillis();
    }

    public DataAnalysisResult(List<Map<String, Object>> data, List<String> headers, String fileName) {
        this();
        this.data = data;
        this.headers = headers;
        this.fileName = fileName;
        this.totalRows = data.size();
        this.totalColumns = headers.size();
    }

    // Getters and Setters
    public List<Map<String, Object>> getData() { return data; }
    public void setData(List<Map<String, Object>> data) { this.data = data; }

    public List<String> getHeaders() { return headers; }
    public void setHeaders(List<String> headers) { this.headers = headers; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Map<String, ColumnStatistics> getStatistics() { return statistics; }
    public void setStatistics(Map<String, ColumnStatistics> statistics) { this.statistics = statistics; }
    
    public Map<String, AdvancedColumnStatistics> getAdvancedStatistics() { return advancedStatistics; }
    public void setAdvancedStatistics(Map<String, AdvancedColumnStatistics> advancedStatistics) { this.advancedStatistics = advancedStatistics; }
    
    public CorrelationAnalysis getCorrelationAnalysis() { return correlationAnalysis; }
    public void setCorrelationAnalysis(CorrelationAnalysis correlationAnalysis) { this.correlationAnalysis = correlationAnalysis; }
    
    public List<RegressionAnalysis> getRegressionAnalyses() { return regressionAnalyses; }
    public void setRegressionAnalyses(List<RegressionAnalysis> regressionAnalyses) { this.regressionAnalyses = regressionAnalyses; }

    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int totalRows) { this.totalRows = totalRows; }

    public int getTotalColumns() { return totalColumns; }
    public void setTotalColumns(int totalColumns) { this.totalColumns = totalColumns; }
    
    public long getAnalysisTimestamp() { return analysisTimestamp; }
    public void setAnalysisTimestamp(long analysisTimestamp) { this.analysisTimestamp = analysisTimestamp; }
    
    public String getDataQualityScore() { return dataQualityScore; }
    public void setDataQualityScore(String dataQualityScore) { this.dataQualityScore = dataQualityScore; }
    
    public List<String> getDataQualityIssues() { return dataQualityIssues; }
    public void setDataQualityIssues(List<String> dataQualityIssues) { this.dataQualityIssues = dataQualityIssues; }
    
    public Map<String, Object> getSummaryStatistics() { return summaryStatistics; }
    public void setSummaryStatistics(Map<String, Object> summaryStatistics) { this.summaryStatistics = summaryStatistics; }
}

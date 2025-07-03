
package com.example.excelanalyzer.model;

import java.util.List;
import java.util.Map;

public class DataAnalysisResult {
    private List<Map<String, Object>> data;
    private List<String> headers;
    private String fileName;
    private Map<String, ColumnStatistics> statistics;
    private int totalRows;
    private int totalColumns;

    // Constructors
    public DataAnalysisResult() {}

    public DataAnalysisResult(List<Map<String, Object>> data, List<String> headers, String fileName) {
        this.data = data;
        this.headers = headers;
        this.fileName = fileName;
        this.totalRows = data.size();
        this.totalColumns = headers.size();
    }

    // Getters and Setters
    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, ColumnStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, ColumnStatistics> statistics) {
        this.statistics = statistics;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(int totalColumns) {
        this.totalColumns = totalColumns;
    }
}

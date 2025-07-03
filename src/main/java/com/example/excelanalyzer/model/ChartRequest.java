
package com.example.excelanalyzer.model;

import java.util.List;
import java.util.Map;

public class ChartRequest {
    private String chartType; // BAR, LINE, PIE, SCATTER, AREA, HISTOGRAM
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private List<String> categories;
    private Map<String, List<Number>> seriesData;
    private int width = 800;
    private int height = 600;
    private Map<String, Object> customOptions;
    
    // Constructors
    public ChartRequest() {}
    
    public ChartRequest(String chartType, String title) {
        this.chartType = chartType;
        this.title = title;
    }
    
    // Getters and Setters
    public String getChartType() { return chartType; }
    public void setChartType(String chartType) { this.chartType = chartType; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getXAxisLabel() { return xAxisLabel; }
    public void setXAxisLabel(String xAxisLabel) { this.xAxisLabel = xAxisLabel; }
    
    public String getYAxisLabel() { return yAxisLabel; }
    public void setYAxisLabel(String yAxisLabel) { this.yAxisLabel = yAxisLabel; }
    
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
    
    public Map<String, List<Number>> getSeriesData() { return seriesData; }
    public void setSeriesData(Map<String, List<Number>> seriesData) { this.seriesData = seriesData; }
    
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    
    public Map<String, Object> getCustomOptions() { return customOptions; }
    public void setCustomOptions(Map<String, Object> customOptions) { this.customOptions = customOptions; }
}

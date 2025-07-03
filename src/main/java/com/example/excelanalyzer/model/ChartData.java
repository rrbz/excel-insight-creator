
package com.example.excelanalyzer.model;

public class ChartData {
    private byte[] imageData;
    private String imageFormat;
    private String title;
    private String chartType;
    private long timestamp;
    
    // Constructors
    public ChartData() {
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }
    
    public String getImageFormat() { return imageFormat; }
    public void setImageFormat(String imageFormat) { this.imageFormat = imageFormat; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getChartType() { return chartType; }
    public void setChartType(String chartType) { this.chartType = chartType; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

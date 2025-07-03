
package com.example.excelanalyzer.model;

import java.util.List;
import java.util.Map;

public class AdvancedColumnStatistics {
    private String columnName;
    private String dataType; // NUMERIC, TEXT, DATE, BOOLEAN
    private int totalCount;
    private int nullCount;
    private int uniqueCount;
    
    // 数值统计
    private Double mean;
    private Double median;
    private Double mode;
    private Double standardDeviation;
    private Double variance;
    private Double min;
    private Double max;
    private Double q1; // 第一四分位数
    private Double q3; // 第三四分位数
    private Double skewness; // 偏度
    private Double kurtosis; // 峰度
    
    // 文本统计
    private Integer averageLength;
    private Integer maxLength;
    private Integer minLength;
    private Map<String, Integer> frequencyDistribution;
    
    // 数据质量
    private Double completenessRatio;
    private List<String> outliers;
    private Boolean hasPattern;
    private String patternDescription;
    
    // 时间序列相关（如果是日期类型）
    private String dateRange;
    private String frequency; // DAILY, WEEKLY, MONTHLY, etc.
    
    // Constructors
    public AdvancedColumnStatistics() {}
    
    public AdvancedColumnStatistics(String columnName, String dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }
    
    // Getters and Setters
    public String getColumnName() { return columnName; }
    public void setColumnName(String columnName) { this.columnName = columnName; }
    
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    
    public int getNullCount() { return nullCount; }
    public void setNullCount(int nullCount) { this.nullCount = nullCount; }
    
    public int getUniqueCount() { return uniqueCount; }
    public void setUniqueCount(int uniqueCount) { this.uniqueCount = uniqueCount; }
    
    public Double getMean() { return mean; }
    public void setMean(Double mean) { this.mean = mean; }
    
    public Double getMedian() { return median; }
    public void setMedian(Double median) { this.median = median; }
    
    public Double getMode() { return mode; }
    public void setMode(Double mode) { this.mode = mode; }
    
    public Double getStandardDeviation() { return standardDeviation; }
    public void setStandardDeviation(Double standardDeviation) { this.standardDeviation = standardDeviation; }
    
    public Double getVariance() { return variance; }
    public void setVariance(Double variance) { this.variance = variance; }
    
    public Double getMin() { return min; }
    public void setMin(Double min) { this.min = min; }
    
    public Double getMax() { return max; }
    public void setMax(Double max) { this.max = max; }
    
    public Double getQ1() { return q1; }
    public void setQ1(Double q1) { this.q1 = q1; }
    
    public Double getQ3() { return q3; }
    public void setQ3(Double q3) { this.q3 = q3; }
    
    public Double getSkewness() { return skewness; }
    public void setSkewness(Double skewness) { this.skewness = skewness; }
    
    public Double getKurtosis() { return kurtosis; }
    public void setKurtosis(Double kurtosis) { this.kurtosis = kurtosis; }
    
    public Integer getAverageLength() { return averageLength; }
    public void setAverageLength(Integer averageLength) { this.averageLength = averageLength; }
    
    public Integer getMaxLength() { return maxLength; }
    public void setMaxLength(Integer maxLength) { this.maxLength = maxLength; }
    
    public Integer getMinLength() { return minLength; }
    public void setMinLength(Integer minLength) { this.minLength = minLength; }
    
    public Map<String, Integer> getFrequencyDistribution() { return frequencyDistribution; }
    public void setFrequencyDistribution(Map<String, Integer> frequencyDistribution) { this.frequencyDistribution = frequencyDistribution; }
    
    public Double getCompletenessRatio() { return completenessRatio; }
    public void setCompletenessRatio(Double completenessRatio) { this.completenessRatio = completenessRatio; }
    
    public List<String> getOutliers() { return outliers; }
    public void setOutliers(List<String> outliers) { this.outliers = outliers; }
    
    public Boolean getHasPattern() { return hasPattern; }
    public void setHasPattern(Boolean hasPattern) { this.hasPattern = hasPattern; }
    
    public String getPatternDescription() { return patternDescription; }
    public void setPatternDescription(String patternDescription) { this.patternDescription = patternDescription; }
    
    public String getDateRange() { return dateRange; }
    public void setDateRange(String dateRange) { this.dateRange = dateRange; }
    
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
}


package com.example.excelanalyzer.service;

import com.example.excelanalyzer.model.AdvancedColumnStatistics;
import com.example.excelanalyzer.model.CorrelationAnalysis;
import com.example.excelanalyzer.model.RegressionAnalysis;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdvancedStatisticalAnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(AdvancedStatisticalAnalysisService.class);
    
    public AdvancedColumnStatistics calculateAdvancedStatistics(List<Object> values, String columnName) {
        AdvancedColumnStatistics stats = new AdvancedColumnStatistics(columnName, detectDataType(values));
        
        // 基础统计
        stats.setTotalCount(values.size());
        stats.setNullCount((int) values.stream().filter(Objects::isNull).count());
        stats.setUniqueCount((int) values.stream().filter(Objects::nonNull).distinct().count());
        stats.setCompletenessRatio((double) (stats.getTotalCount() - stats.getNullCount()) / stats.getTotalCount());
        
        List<Object> nonNullValues = values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        
        if (stats.getDataType().equals("NUMERIC")) {
            calculateNumericStatistics(nonNullValues, stats);
        } else if (stats.getDataType().equals("TEXT")) {
            calculateTextStatistics(nonNullValues, stats);
        }
        
        // 数据质量分析
        analyzeDataQuality(nonNullValues, stats);
        
        return stats;
    }
    
    private void calculateNumericStatistics(List<Object> values, AdvancedColumnStatistics stats) {
        double[] numericValues = values.stream()
                .filter(v -> isNumeric(v.toString()))
                .mapToDouble(v -> Double.parseDouble(v.toString()))
                .toArray();
        
        if (numericValues.length == 0) return;
        
        DescriptiveStatistics descriptiveStats = new DescriptiveStatistics(numericValues);
        
        // 基础统计量
        stats.setMean(descriptiveStats.getMean());
        stats.setMedian(descriptiveStats.getPercentile(50));
        stats.setStandardDeviation(descriptiveStats.getStandardDeviation());
        stats.setVariance(descriptiveStats.getVariance());
        stats.setMin(descriptiveStats.getMin());
        stats.setMax(descriptiveStats.getMax());
        
        // 四分位数
        stats.setQ1(descriptiveStats.getPercentile(25));
        stats.setQ3(descriptiveStats.getPercentile(75));
        
        // 偏度和峰度
        Skewness skewness = new Skewness();
        Kurtosis kurtosis = new Kurtosis();
        stats.setSkewness(skewness.evaluate(numericValues));
        stats.setKurtosis(kurtosis.evaluate(numericValues));
        
        // 众数计算
        stats.setMode(calculateMode(numericValues));
        
        // 异常值检测
        detectOutliers(numericValues, stats);
        
        logger.debug("计算数值列 {} 的高级统计量完成", stats.getColumnName());
    }
    
    private void calculateTextStatistics(List<Object> values, AdvancedColumnStatistics stats) {
        List<String> stringValues = values.stream()
                .map(Object::toString)
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.toList());
        
        if (stringValues.isEmpty()) return;
        
        // 长度统计
        int[] lengths = stringValues.stream().mapToInt(String::length).toArray();
        stats.setAverageLength((int) Arrays.stream(lengths).average().orElse(0));
        stats.setMaxLength(Arrays.stream(lengths).max().orElse(0));
        stats.setMinLength(Arrays.stream(lengths).min().orElse(0));
        
        // 频率分布
        Map<String, Integer> frequencyMap = stringValues.stream()
                .collect(Collectors.groupingBy(
                        s -> s,
                        Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
        stats.setFrequencyDistribution(frequencyMap);
        
        // 模式检测
        detectTextPatterns(stringValues, stats);
        
        logger.debug("计算文本列 {} 的统计量完成", stats.getColumnName());
    }
    
    public CorrelationAnalysis performCorrelationAnalysis(Map<String, List<Double>> numericData, String method) {
        CorrelationAnalysis analysis = new CorrelationAnalysis(method);
        
        List<String> columns = new ArrayList<>(numericData.keySet());
        int size = columns.size();
        
        // 创建相关系数矩阵
        Map<String, Map<String, Double>> correlationMatrix = new HashMap<>();
        
        for (int i = 0; i < size; i++) {
            String col1 = columns.get(i);
            Map<String, Double> row = new HashMap<>();
            
            for (int j = 0; j < size; j++) {
                String col2 = columns.get(j);
                double correlation = calculateCorrelation(
                        numericData.get(col1), 
                        numericData.get(col2), 
                        method
                );
                row.put(col2, correlation);
            }
            correlationMatrix.put(col1, row);
        }
        
        analysis.setCorrelationMatrix(correlationMatrix);
        analysis.setStrongCorrelations(findStrongCorrelations(correlationMatrix));
        analysis.setInterpretation(generateCorrelationInterpretation(correlationMatrix));
        
        logger.info("完成 {} 相关性分析，涉及 {} 个变量", method, size);
        return analysis;
    }
    
    public RegressionAnalysis performLinearRegression(List<Double> dependentVar, 
                                                     Map<String, List<Double>> independentVars, 
                                                     String dependentVarName) {
        RegressionAnalysis analysis = new RegressionAnalysis("LINEAR", dependentVarName);
        
        // 简单线性回归（单变量）
        if (independentVars.size() == 1) {
            String independentVarName = independentVars.keySet().iterator().next();
            List<Double> independentVar = independentVars.get(independentVarName);
            
            SimpleRegression regression = new SimpleRegression();
            
            for (int i = 0; i < dependentVar.size(); i++) {
                regression.addData(independentVar.get(i), dependentVar.get(i));
            }
            
            // 设置结果
            Map<String, Double> coefficients = new HashMap<>();
            coefficients.put("intercept", regression.getIntercept());
            coefficients.put(independentVarName, regression.getSlope());
            
            analysis.setCoefficients(coefficients);
            analysis.setRSquared(regression.getRSquare());
            analysis.setEquation(String.format("y = %.4f + %.4f * %s", 
                    regression.getIntercept(), regression.getSlope(), independentVarName));
            
            // 计算残差
            List<Double> residuals = new ArrayList<>();
            for (int i = 0; i < dependentVar.size(); i++) {
                double predicted = regression.predict(independentVar.get(i));
                residuals.add(dependentVar.get(i) - predicted);
            }
            analysis.setResiduals(residuals);
            
            analysis.setIndependentVariables(List.of(independentVarName));
            analysis.setModelSummary(generateRegressionSummary(analysis));
        }
        
        logger.info("完成线性回归分析，因变量: {}", dependentVarName);
        return analysis;
    }
    
    private String detectDataType(List<Object> values) {
        if (values == null || values.isEmpty()) return "UNKNOWN";
        
        long numericCount = values.stream()
                .filter(Objects::nonNull)
                .filter(v -> isNumeric(v.toString()))
                .count();
        
        long totalNonNull = values.stream().filter(Objects::nonNull).count();
        
        if (totalNonNull == 0) return "UNKNOWN";
        
        // 如果70%以上是数值，认为是数值类型
        if ((double) numericCount / totalNonNull > 0.7) {
            return "NUMERIC";
        }
        
        // 检查是否是日期
        long dateCount = values.stream()
                .filter(Objects::nonNull)
                .filter(v -> isDate(v.toString()))
                .count();
        
        if ((double) dateCount / totalNonNull > 0.7) {
            return "DATE";
        }
        
        return "TEXT";
    }
    
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean isDate(String str) {
        // 简单的日期检测逻辑
        return str.matches("\\d{4}-\\d{2}-\\d{2}") || 
               str.matches("\\d{2}/\\d{2}/\\d{4}") ||
               str.matches("\\d{4}/\\d{2}/\\d{2}");
    }
    
    private Double calculateMode(double[] values) {
        Map<Double, Integer> frequencyMap = new HashMap<>();
        for (double value : values) {
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }
        
        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    
    private void detectOutliers(double[] values, AdvancedColumnStatistics stats) {
        DescriptiveStatistics descriptiveStats = new DescriptiveStatistics(values);
        double q1 = descriptiveStats.getPercentile(25);
        double q3 = descriptiveStats.getPercentile(75);
        double iqr = q3 - q1;
        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;
        
        List<String> outliers = Arrays.stream(values)
                .filter(v -> v < lowerBound || v > upperBound)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        
        stats.setOutliers(outliers);
    }
    
    private void detectTextPatterns(List<String> values, AdvancedColumnStatistics stats) {
        // 检测常见模式
        boolean hasEmailPattern = values.stream().anyMatch(s -> s.contains("@"));
        boolean hasPhonePattern = values.stream().anyMatch(s -> s.matches("\\d{3}-\\d{3}-\\d{4}"));
        boolean hasDatePattern = values.stream().anyMatch(this::isDate);
        
        if (hasEmailPattern) {
            stats.setHasPattern(true);
            stats.setPatternDescription("包含邮箱地址格式");
        } else if (hasPhonePattern) {
            stats.setHasPattern(true);
            stats.setPatternDescription("包含电话号码格式");
        } else if (hasDatePattern) {
            stats.setHasPattern(true);
            stats.setPatternDescription("包含日期格式");
        }
    }
    
    private void analyzeDataQuality(List<Object> values, AdvancedColumnStatistics stats) {
        // 数据质量分析逻辑
        double completeness = stats.getCompletenessRatio();
        
        if (completeness >= 0.95) {
            stats.setPatternDescription("数据完整性: 优秀");
        } else if (completeness >= 0.8) {
            stats.setPatternDescription("数据完整性: 良好");
        } else if (completeness >= 0.6) {
            stats.setPatternDescription("数据完整性: 一般");
        } else {
            stats.setPatternDescription("数据完整性: 较差");
        }
    }
    
    private double calculateCorrelation(List<Double> x, List<Double> y, String method) {
        double[] xArray = x.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yArray = y.stream().mapToDouble(Double::doubleValue).toArray();
        
        switch (method.toUpperCase()) {
            case "PEARSON":
                PearsonsCorrelation pearson = new PearsonsCorrelation();
                return pearson.correlation(xArray, yArray);
            case "SPEARMAN":
                SpearmansCorrelation spearman = new SpearmansCorrelation();
                return spearman.correlation(xArray, yArray);
            default:
                return new PearsonsCorrelation().correlation(xArray, yArray);
        }
    }
    
    private Map<String, String> findStrongCorrelations(Map<String, Map<String, Double>> correlationMatrix) {
        Map<String, String> strongCorrelations = new HashMap<>();
        
        for (Map.Entry<String, Map<String, Double>> entry1 : correlationMatrix.entrySet()) {
            String var1 = entry1.getKey();
            for (Map.Entry<String, Double> entry2 : entry1.getValue().entrySet()) {
                String var2 = entry2.getKey();
                Double correlation = entry2.getValue();
                
                if (!var1.equals(var2) && Math.abs(correlation) > 0.7) {
                    String key = var1.compareTo(var2) < 0 ? var1 + "-" + var2 : var2 + "-" + var1;
                    String strength = Math.abs(correlation) > 0.9 ? "极强" : "强";
                    String direction = correlation > 0 ? "正相关" : "负相关";
                    strongCorrelations.put(key, String.format("%s%s (%.3f)", strength, direction, correlation));
                }
            }
        }
        
        return strongCorrelations;
    }
    
    private String generateCorrelationInterpretation(Map<String, Map<String, Double>> correlationMatrix) {
        StringBuilder interpretation = new StringBuilder();
        interpretation.append("相关性分析结果解读:\n");
        
        Map<String, String> strongCorr = findStrongCorrelations(correlationMatrix);
        if (strongCorr.isEmpty()) {
            interpretation.append("- 变量间未发现强相关关系\n");
        } else {
            interpretation.append("- 发现以下强相关关系:\n");
            strongCorr.forEach((pair, desc) -> 
                interpretation.append(String.format("  %s: %s\n", pair, desc)));
        }
        
        return interpretation.toString();
    }
    
    private String generateRegressionSummary(RegressionAnalysis analysis) {
        StringBuilder summary = new StringBuilder();
        summary.append("线性回归分析摘要:\n");
        summary.append(String.format("- 回归方程: %s\n", analysis.getEquation()));
        summary.append(String.format("- 决定系数 R²: %.4f\n", analysis.getRSquared()));
        
        if (analysis.getRSquared() > 0.8) {
            summary.append("- 模型拟合度: 优秀\n");
        } else if (analysis.getRSquared() > 0.6) {
            summary.append("- 模型拟合度: 良好\n");
        } else if (analysis.getRSquared() > 0.4) {
            summary.append("- 模型拟合度: 一般\n");
        } else {
            summary.append("- 模型拟合度: 较差\n");
        }
        
        return summary.toString();
    }
}

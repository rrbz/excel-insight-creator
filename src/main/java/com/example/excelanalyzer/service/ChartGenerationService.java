
package com.example.excelanalyzer.service;

import com.example.excelanalyzer.model.ChartData;
import com.example.excelanalyzer.model.ChartRequest;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Service
public class ChartGenerationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ChartGenerationService.class);
    
    public ChartData generateChart(ChartRequest request) throws IOException {
        logger.info("生成图表: 类型={}, 标题={}", request.getChartType(), request.getTitle());
        
        JFreeChart chart = createChart(request);
        
        // 自定义图表样式
        customizeChart(chart, request);
        
        // 生成图表图片
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(outputStream, chart, request.getWidth(), request.getHeight());
        
        ChartData chartData = new ChartData();
        chartData.setImageData(outputStream.toByteArray());
        chartData.setImageFormat("PNG");
        chartData.setTitle(request.getTitle());
        chartData.setChartType(request.getChartType());
        
        logger.info("图表生成完成: {}", request.getTitle());
        return chartData;
    }
    
    private JFreeChart createChart(ChartRequest request) {
        switch (request.getChartType().toUpperCase()) {
            case "BAR":
                return createBarChart(request);
            case "LINE":
                return createLineChart(request);
            case "PIE":
                return createPieChart(request);
            case "SCATTER":
                return createScatterChart(request);
            case "AREA":
                return createAreaChart(request);
            case "HISTOGRAM":
                return createHistogram(request);
            default:
                throw new IllegalArgumentException("不支持的图表类型: " + request.getChartType());
        }
    }
    
    private JFreeChart createBarChart(ChartRequest request) {
        CategoryDataset dataset = createCategoryDataset(request);
        
        JFreeChart chart = ChartFactory.createBarChart(
            request.getTitle(),
            request.getXAxisLabel(),
            request.getYAxisLabel(),
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // 自定义柱状图样式
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189));
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        
        return chart;
    }
    
    private JFreeChart createLineChart(ChartRequest request) {
        CategoryDataset dataset = createCategoryDataset(request);
        
        return ChartFactory.createLineChart(
            request.getTitle(),
            request.getXAxisLabel(),
            request.getYAxisLabel(),
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
    }
    
    private JFreeChart createPieChart(ChartRequest request) {
        PieDataset dataset = createPieDataset(request);
        
        return ChartFactory.createPieChart(
            request.getTitle(),
            dataset,
            true,
            true,
            false
        );
    }
    
    private JFreeChart createScatterChart(ChartRequest request) {
        XYDataset dataset = createXYDataset(request);
        
        return ChartFactory.createScatterPlot(
            request.getTitle(),
            request.getXAxisLabel(),
            request.getYAxisLabel(),
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
    }
    
    private JFreeChart createAreaChart(ChartRequest request) {
        CategoryDataset dataset = createCategoryDataset(request);
        
        return ChartFactory.createAreaChart(
            request.getTitle(),
            request.getXAxisLabel(),
            request.getYAxisLabel(),
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
    }
    
    private JFreeChart createHistogram(ChartRequest request) {
        // 简化的直方图实现
        CategoryDataset dataset = createCategoryDataset(request);
        
        JFreeChart chart = ChartFactory.createBarChart(
            request.getTitle(),
            request.getXAxisLabel(),
            "频率",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        // 设置直方图样式
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(0.0); // 去除柱子间的间隔
        
        return chart;
    }
    
    private CategoryDataset createCategoryDataset(ChartRequest request) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        Map<String, List<Number>> seriesData = request.getSeriesData();
        List<String> categories = request.getCategories();
        
        for (Map.Entry<String, List<Number>> entry : seriesData.entrySet()) {
            String seriesName = entry.getKey();
            List<Number> values = entry.getValue();
            
            for (int i = 0; i < values.size() && i < categories.size(); i++) {
                dataset.addValue(values.get(i), seriesName, categories.get(i));
            }
        }
        
        return dataset;
    }
    
    private PieDataset createPieDataset(ChartRequest request) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        List<String> categories = request.getCategories();
        Map<String, List<Number>> seriesData = request.getSeriesData();
        
        if (!seriesData.isEmpty()) {
            List<Number> values = seriesData.values().iterator().next();
            
            for (int i = 0; i < categories.size() && i < values.size(); i++) {
                dataset.setValue(categories.get(i), values.get(i));
            }
        }
        
        return dataset;
    }
    
    private XYDataset createXYDataset(ChartRequest request) {
        DefaultXYDataset dataset = new DefaultXYDataset();
        
        Map<String, List<Number>> seriesData = request.getSeriesData();
        
        for (Map.Entry<String, List<Number>> entry : seriesData.entrySet()) {
            String seriesName = entry.getKey();
            List<Number> values = entry.getValue();
            
            double[][] data = new double[2][values.size()];
            for (int i = 0; i < values.size(); i++) {
                data[0][i] = i; // X轴值
                data[1][i] = values.get(i).doubleValue(); // Y轴值
            }
            
            dataset.addSeries(seriesName, data);
        }
        
        return dataset;
    }
    
    private void customizeChart(JFreeChart chart, ChartRequest request) {
        // 设置图表背景
        chart.setBackgroundPaint(Color.WHITE);
        
        // 设置标题字体
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 16));
        
        // 根据图表类型进行特定的样式设置
        if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            
            // 设置网格线
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            
            // 设置轴标签字体
            CategoryAxis domainAxis = plot.getDomainAxis();
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            
            domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
            rangeAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
            
        } else if (chart.getPlot() instanceof XYPlot) {
            XYPlot plot = (XYPlot) chart.getPlot();
            
            // 设置散点图样式
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        }
    }
    
    public Map<String, Object> generateMultipleCharts(List<ChartRequest> requests) {
        Map<String, Object> result = new HashMap<>();
        List<ChartData> charts = new ArrayList<>();
        
        for (ChartRequest request : requests) {
            try {
                ChartData chartData = generateChart(request);
                charts.add(chartData);
            } catch (IOException e) {
                logger.error("生成图表失败: {}", e.getMessage(), e);
            }
        }
        
        result.put("charts", charts);
        result.put("totalCount", charts.size());
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
    
    public ChartData generateCorrelationHeatmap(Map<String, Map<String, Double>> correlationMatrix) throws IOException {
        logger.info("生成相关性热力图");
        
        // 创建热力图数据
        ChartRequest request = new ChartRequest();
        request.setChartType("HEATMAP");
        request.setTitle("变量相关性热力图");
        request.setWidth(800);
        request.setHeight(600);
        
        // 简化实现：使用柱状图代替热力图
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (Map.Entry<String, Map<String, Double>> entry : correlationMatrix.entrySet()) {
            String var1 = entry.getKey();
            for (Map.Entry<String, Double> innerEntry : entry.getValue().entrySet()) {
                String var2 = innerEntry.getKey();
                Double correlation = innerEntry.getValue();
                
                if (!var1.equals(var2)) {
                    dataset.addValue(Math.abs(correlation), var1, var2);
                }
            }
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "变量相关性矩阵",
            "变量组合",
            "相关系数绝对值",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        customizeChart(chart, request);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(outputStream, chart, 800, 600);
        
        ChartData chartData = new ChartData();
        chartData.setImageData(outputStream.toByteArray());
        chartData.setImageFormat("PNG");
        chartData.setTitle("相关性热力图");
        chartData.setChartType("HEATMAP");
        
        return chartData;
    }
}

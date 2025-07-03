
package com.example.excelanalyzer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_results")
public class AnalysisResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id")
    private DataSet dataSet;
    
    @Column(nullable = false)
    private String analysisType; // STATISTICAL, CORRELATION, REGRESSION, CLUSTERING, etc.
    
    @Column(nullable = false)
    private LocalDateTime createdTime;
    
    @Lob
    @Column(columnDefinition = "CLOB")
    private String resultData; // JSON格式的分析结果
    
    @Lob
    @Column(columnDefinition = "CLOB")
    private String parameters; // 分析参数
    
    @Column
    private String status; // SUCCESS, FAILED, PROCESSING
    
    @Column
    private String errorMessage;
    
    // Constructors
    public AnalysisResult() {
        this.createdTime = LocalDateTime.now();
        this.status = "PROCESSING";
    }
    
    public AnalysisResult(DataSet dataSet, String analysisType) {
        this();
        this.dataSet = dataSet;
        this.analysisType = analysisType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public DataSet getDataSet() { return dataSet; }
    public void setDataSet(DataSet dataSet) { this.dataSet = dataSet; }
    
    public String getAnalysisType() { return analysisType; }
    public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public String getResultData() { return resultData; }
    public void setResultData(String resultData) { this.resultData = resultData; }
    
    public String getParameters() { return parameters; }
    public void setParameters(String parameters) { this.parameters = parameters; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

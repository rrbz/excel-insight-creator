
package com.example.excelanalyzer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "datasets")
public class DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false)
    private String originalFileName;
    
    @Column(nullable = false)
    private Long fileSize;
    
    @Column(nullable = false)
    private String fileType;
    
    @Column(nullable = false)
    private Integer totalRows;
    
    @Column(nullable = false)
    private Integer totalColumns;
    
    @Column(nullable = false)
    private LocalDateTime uploadTime;
    
    @Column(nullable = false)
    private LocalDateTime lastAnalyzed;
    
    @Lob
    @Column(columnDefinition = "CLOB")
    private String headers;
    
    @Column(nullable = false)
    private String status; // UPLOADED, ANALYZING, ANALYZED, ERROR
    
    @OneToMany(mappedBy = "dataSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AnalysisResult> analysisResults;
    
    // Constructors
    public DataSet() {
        this.uploadTime = LocalDateTime.now();
        this.lastAnalyzed = LocalDateTime.now();
        this.status = "UPLOADED";
    }
    
    public DataSet(String fileName, String originalFileName, Long fileSize, String fileType) {
        this();
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public Integer getTotalRows() { return totalRows; }
    public void setTotalRows(Integer totalRows) { this.totalRows = totalRows; }
    
    public Integer getTotalColumns() { return totalColumns; }
    public void setTotalColumns(Integer totalColumns) { this.totalColumns = totalColumns; }
    
    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }
    
    public LocalDateTime getLastAnalyzed() { return lastAnalyzed; }
    public void setLastAnalyzed(LocalDateTime lastAnalyzed) { this.lastAnalyzed = lastAnalyzed; }
    
    public String getHeaders() { return headers; }
    public void setHeaders(String headers) { this.headers = headers; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<AnalysisResult> getAnalysisResults() { return analysisResults; }
    public void setAnalysisResults(List<AnalysisResult> analysisResults) { this.analysisResults = analysisResults; }
}

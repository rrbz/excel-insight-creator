
package com.example.excelanalyzer.repository;

import com.example.excelanalyzer.entity.AnalysisResult;
import com.example.excelanalyzer.entity.DataSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {
    
    List<AnalysisResult> findByDataSet(DataSet dataSet);
    
    List<AnalysisResult> findByAnalysisType(String analysisType);
    
    List<AnalysisResult> findByStatus(String status);
    
    List<AnalysisResult> findByDataSetAndAnalysisType(DataSet dataSet, String analysisType);
    
    List<AnalysisResult> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT ar FROM AnalysisResult ar WHERE ar.dataSet.id = :datasetId ORDER BY ar.createdTime DESC")
    List<AnalysisResult> findByDataSetIdOrderByCreatedTimeDesc(@Param("datasetId") Long datasetId);
    
    @Query("SELECT COUNT(ar) FROM AnalysisResult ar WHERE ar.analysisType = :analysisType AND ar.status = 'SUCCESS'")
    Long countSuccessfulAnalysesByType(@Param("analysisType") String analysisType);
}

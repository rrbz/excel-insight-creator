
package com.example.excelanalyzer.repository;

import com.example.excelanalyzer.entity.DataSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataSetRepository extends JpaRepository<DataSet, Long> {
    
    List<DataSet> findByStatus(String status);
    
    List<DataSet> findByFileType(String fileType);
    
    List<DataSet> findByUploadTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    Optional<DataSet> findByFileName(String fileName);
    
    @Query("SELECT d FROM DataSet d WHERE d.originalFileName LIKE %:filename%")
    List<DataSet> findByOriginalFileNameContaining(@Param("filename") String filename);
    
    @Query("SELECT d FROM DataSet d ORDER BY d.uploadTime DESC")
    List<DataSet> findAllOrderByUploadTimeDesc();
    
    @Query("SELECT COUNT(d) FROM DataSet d WHERE d.status = :status")
    Long countByStatus(@Param("status") String status);
    
    @Query("SELECT SUM(d.fileSize) FROM DataSet d")
    Long getTotalFileSize();
}

package com.kshem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kshem.entity.JobApplication;
import com.kshem.enums.ApplicationStatus;

@Repository
public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    @Query("SELECT j FROM JobApplication j WHERE j.student.id = :studentId AND j.jobDrive.id = :jobDriveId")
	Optional<JobApplication> findByStudent_IdAndJobDrive_Id(
            Long studentId, Long jobDriveId);

    @Query("SELECT j.id FROM JobApplication j WHERE j.student.id = :studentId AND j.jobDrive.id = :jobDriveId")
	boolean existsByStudent_IdAndJobDrive_Id(
            Long studentId, Long jobDriveId);

    @Query("SELECT j FROM JobApplication j WHERE j.student.id = :studentId")
	List<JobApplication> findByStudent_Id(Long studentId);

    @Query("SELECT j FROM JobApplication j WHERE j.student.id = :studentId")
	Page<JobApplication> findByStudent_Id(
            Long studentId, Pageable pageable);

    @Query("SELECT j FROM JobApplication j WHERE j.jobDrive.id = :jobDriveId")
	List<JobApplication> findByJobDrive_Id(Long jobDriveId);

    @Query("SELECT j FROM JobApplication j WHERE j.jobDrive.id = :jobDriveId")
	Page<JobApplication> findByJobDrive_Id(
            Long jobDriveId, Pageable pageable);

    @Query("SELECT j FROM JobApplication j WHERE j.status = :status")
	List<JobApplication> findByStatus(ApplicationStatus status);

    
    Page<JobApplication> findByStatus(ApplicationStatus status, Pageable pageable);

    
    @Query("SELECT j FROM JobApplication j WHERE j.jobDrive.id = :jobDriveId AND j.status = :status")
	List<JobApplication> findByJobDrive_IdAndStatus(Long jobDriveId, ApplicationStatus status);

    
    @Query("SELECT COUNT(j) FROM JobApplication j WHERE j.jobDrive.id = :jobDriveId AND j.status = :status")
	long countByJobDrive_IdAndStatus(Long jobDriveId, ApplicationStatus status);

    
    
    @Query("""
            SELECT a FROM JobApplication a
            WHERE LOWER(a.student.fullName)
            LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<JobApplication> searchByStudentName(@Param("name") String name, Pageable pageable);

    
    
    @Query("""
            SELECT COUNT(a) FROM JobApplication a
            WHERE a.status = :status
            """)
    long countApplicationsByStatus(@Param("status") ApplicationStatus status);

    
    
    
    @Modifying
    @Transactional
    @Query("""
            UPDATE JobApplication a
            SET a.status = :status
            WHERE a.id = :applicationId
            """)
    int updateApplicationStatus(
            @Param("applicationId") Long applicationId,
            @Param("status") ApplicationStatus status);
}
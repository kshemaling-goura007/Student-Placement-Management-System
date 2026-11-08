package com.kshem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kshem.entity.Interview;
import com.kshem.enums.InterviewResult;
import com.kshem.enums.InterviewStatus;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    // 1. Find interviews by application
    List<Interview> findByApplication_Id(Long applicationId);

    // 2. Find interviews by application with pagination
    Page<Interview> findByApplication_Id(Long applicationId, Pageable pageable);

    // 3. Find interviews by interview status
    List<Interview> findByStatus(InterviewStatus status);

    // 4. Find interviews by result
    List<Interview> findByResult(InterviewResult result);

    // 5. Find interviews by status and result
    List<Interview> findByStatusAndResult(
            InterviewStatus status, InterviewResult result);

    // 6. Find interviews between dates
    List<Interview> findByInterviewDateBetween(
            LocalDateTime startDate, LocalDateTime endDate);

    // 7. Find interviews by interviewer
    List<Interview> findByInterviewerNameContainingIgnoreCase(
            String interviewerName);

    // 8. Find interviews by round name
    List<Interview> findByRoundNameContainingIgnoreCase(String roundName);

    // 9. Find interviews with pagination by status
    Page<Interview> findByStatus(
            InterviewStatus status, Pageable pageable);

    // 10. Check whether an interview exists for an application
    boolean existsByApplication_Id(Long applicationId);

    // 11. Count interviews by application
    long countByApplication_Id(Long applicationId);

    // 12. Find upcoming scheduled interviews
    @Query("""
            SELECT i FROM Interview i
            WHERE i.interviewDate >= :today
            AND i.status = :status
            ORDER BY i.interviewDate ASC
            """)
    List<Interview> findUpcomingInterviews(
            @Param("today") LocalDateTime today,
            @Param("status") InterviewStatus status);

    // 13. Count interviews by result
    @Query("""
            SELECT COUNT(i) FROM Interview i
            WHERE i.result = :result
            """)
    long countInterviewsByResult(
            @Param("result") InterviewResult result);

    // 14. Update interview result
    @Modifying
    @Transactional
    @Query("""
            UPDATE Interview i
            SET i.result = :result
            WHERE i.id = :interviewId
            """)
    int updateInterviewResult(
            @Param("interviewId") Long interviewId,
            @Param("result") InterviewResult result);

    // 15. Update interview status
    @Modifying
    @Transactional
    @Query("""
            UPDATE Interview i
            SET i.status = :status
            WHERE i.id = :interviewId
            """)
    int updateInterviewStatus(
            @Param("interviewId") Long interviewId,
            @Param("status") InterviewStatus status);
}
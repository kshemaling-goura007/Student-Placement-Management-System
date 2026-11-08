
package com.kshem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kshem.entity.JobDrive;
import com.kshem.enums.Department;
import com.kshem.enums.DriveStatus;
import com.kshem.enums.EmploymentType;

@Repository
public interface JobDriveRepository extends JpaRepository<JobDrive, Long> {

    // Find job drives
    List<JobDrive> findByStatus(DriveStatus status);

    List<JobDrive> findByCompanyId(Long companyId);

    Optional<JobDrive> findByIdAndCompanyId(Long id, Long companyId);

    List<JobDrive> findByEmploymentType(EmploymentType employmentType);

    // Existence checks
    boolean existsByJobTitleAndCompanyId(String jobTitle, Long companyId);

    // Search job drives
    List<JobDrive> findByJobTitleContainingIgnoreCase(String keyword);

    List<JobDrive> findByStatusAndEmploymentType(DriveStatus status, EmploymentType employmentType);

    // Date-based filtering
    List<JobDrive> findByApplicationStartDateLessThanEqualAndApplicationEndDateGreaterThanEqual(
                                LocalDate today, LocalDate currentDate);

    List<JobDrive> findByApplicationEndDateBefore(LocalDate date);

    List<JobDrive> findByDriveDateAfter(LocalDate date);

    // Salary filtering
    List<JobDrive> findBySalaryPackageGreaterThanEqual(Double salary);

    List<JobDrive> findBySalaryPackageBetween(Double minSalary, Double maxSalary);

    // CGPA filtering
    List<JobDrive> findByMinimumCgpaLessThanEqual(Double cgpa);

    // Pagination
    Page<JobDrive> findByStatus(DriveStatus status, Pageable pageable);

    Page<JobDrive> findByCompanyId(Long companyId, Pageable pageable);

    // Active drives whose application window is open
    @Query("""
           SELECT j FROM JobDrive j
           WHERE j.status = :status
           AND j.applicationStartDate <= :today
           AND j.applicationEndDate >= :today
           """)
    List<JobDrive> findCurrentlyOpenDrives(@Param("status") DriveStatus status, @Param("today") LocalDate today);

    // Find drives for a particular department
    @Query("""
           SELECT DISTINCT j FROM JobDrive j
           JOIN j.eligibleDepartments d
           WHERE d = :department
           AND j.status = :status
           """)
    List<JobDrive> findDrivesByDepartment(@Param("department") Department department, @Param("status") DriveStatus status);

    // Search by job title or description
    @Query("""
           SELECT j FROM JobDrive j
           WHERE LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(j.jobDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    Page<JobDrive> searchJobDrives(@Param("keyword") String keyword, Pageable pageable);

    // Average salary package
    @Query("SELECT AVG(j.salaryPackage) FROM JobDrive j")
    Double findAverageSalaryPackage();

    
    // Highest salary package
    @Query("SELECT MAX(j.salaryPackage) FROM JobDrive j")
    Double findHighestSalaryPackage();

    
    // Count drives published by a company
    long countByCompanyId(Long companyId);
}


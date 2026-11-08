package com.kshem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kshem.entity.Student;
import com.kshem.enums.Department;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find students
    Optional<Student> findByEmail(String email);

    Optional<Student> findByRegistrationNumber(String registrationNumber);

    List<Student> findByDepartment(Department department);

    List<Student> findByGraduationYear(Integer graduationYear);

    // Existence checks
    boolean existsByEmail(String email);

    boolean existsByRegistrationNumber(String registrationNumber);

    // CGPA-based filtering
    List<Student> findByCgpaGreaterThanEqual(Double cgpa);

    List<Student> findByCgpaBetween(Double minCgpa, Double maxCgpa);

    // Backlog and eligibility filtering
    List<Student> findByActiveBacklogsLessThanEqual(Integer backlogs);
    
    Page<Student> findByPlacementEligibleTrue(Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.placementEligible = TRUE")
	List<Student> findByPlacementEligibleTrue();

    List<Student> findByPlacementEligibleFalse();

    // Combined filtering
    List<Student> findByDepartmentAndGraduationYear(Department department, Integer graduationYear);

    List<Student> findByDepartmentAndCgpaGreaterThanEqualAndActiveBacklogsLessThanEqual(Department department, Double cgpa, Integer backlogs);

    
    // Pagination
    Page<Student> findByDepartment(Department department, Pageable pageable);

    
    Page<Student> findByGraduationYear(Integer graduationYear, Pageable pageable);

    
    
    // Custom JPQL: Eligible students
    @Query("""
           SELECT s FROM Student s
           WHERE s.cgpa >= :minCgpa AND s.activeBacklogs <= :maxBacklogs AND s.graduationYear = :graduationYear
           AND s.placementEligible = true""")
    List<Student> findEligibleStudents(@Param("minCgpa") Double minCgpa, @Param("maxBacklogs") Integer maxBacklogs,
                   @Param("graduationYear") Integer graduationYear);

    
    
    // Count students by department
    @Query("""
           SELECT COUNT(s) FROM Student s
           WHERE s.department = :department""")
    long countByDepartment(@Param("department") Department department);

    
    
    // Search students by name or email
    @Query("""
           SELECT s FROM Student s
           WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    Page<Student> searchStudents(@Param("keyword") String keyword, Pageable pageable);

    
    // Highest CGPA
    @Query("SELECT MAX(s.cgpa) FROM Student s")
    Double findHighestCgpa();

    
    // Average CGPA
    @Query("SELECT AVG(s.cgpa) FROM Student s")
    Double findAverageCgpa();
}

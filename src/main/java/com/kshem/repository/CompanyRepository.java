
package com.kshem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kshem.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    // Find company
    Optional<Company> findByEmail(String email);

    Optional<Company> findByCompanyName(String companyName);

    List<Company> findByIndustry(String industry);

    List<Company> findByLocation(String location);

    // Existence checks
    boolean existsByEmail(String email);

    boolean existsByCompanyName(String companyName);

    // Search companies
    List<Company> findByCompanyNameContainingIgnoreCase(String keyword);

    List<Company> findByIndustryAndLocation(String industry, String location);

    // Pagination
    Page<Company> findByIndustry(String industry, Pageable pageable);

    Page<Company> findByLocation(String location, Pageable pageable);

    // Custom JPQL: Search by name, industry or location
    @Query("""
           SELECT c FROM Company c
           WHERE LOWER(c.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.location) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    Page<Company> searchCompanies(@Param("keyword") String keyword, Pageable pageable );

    // Count companies by industry
    @Query("""
           SELECT COUNT(c) FROM Company c WHERE c.industry = :industry
           """)
    long countByIndustry(@Param("industry") String industry);

    // Find companies registered in a location
    @Query("""
           SELECT c FROM Company c
           WHERE LOWER(c.location) = LOWER(:location) ORDER BY c.companyName ASC
           """)
    List<Company> findCompaniesByLocation(@Param("location") String location);

    // Find company by associated user ID
    @Query("SELECT c FROM Company c WHERE c.user.id = :userId")
    Optional<Company> findByUserId(@Param("userId") Long userId);
    
    
}


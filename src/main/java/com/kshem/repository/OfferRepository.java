package com.kshem.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import com.kshem.entity.Offer;
import com.kshem.enums.OfferStatus;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    // 1. Find offer by application ID
    Optional<Offer> findByApplication_Id(Long applicationId);

    // 2. Check whether an offer exists for an application
    boolean existsByApplication_Id(Long applicationId);

    // 3. Find offers by status
    List<Offer> findByStatus(OfferStatus status);

    // 4. Find offers by status with pagination
    Page<Offer> findByStatus(OfferStatus status, Pageable pageable);

    // 5. Find offers issued between two dates
    List<Offer> findByOfferDateBetween(LocalDate startDate, LocalDate endDate);

    // 6. Find offers expiring before a date
    List<Offer> findByOfferExpiryDateBefore(LocalDate date);

    // 7. Find offers expiring within a date range
    List<Offer> findByOfferExpiryDateBetween(
            LocalDate startDate, LocalDate endDate);

    // 8. Find offers by job designation
    List<Offer> findByJobDesignationContainingIgnoreCase(String jobDesignation);

    // 9. Find offers with salary greater than or equal to a value
    List<Offer> findBySalaryPackageGreaterThanEqual(BigDecimal salary);

    // 10. Find offers by status and salary
    List<Offer> findByStatusAndSalaryPackageGreaterThanEqual(OfferStatus status, BigDecimal salary);

    // 11. Count offers by status
    long countByStatus(OfferStatus status);

    // 12. Find offers by status ordered by offer date
    List<Offer> findByStatusOrderByOfferDateDesc(OfferStatus status);

    // 13. Find expired offers
    @Query("""
            SELECT o FROM Offer o
            WHERE o.offerExpiryDate < :today
            AND o.status = :status
            """)
    List<Offer> findExpiredOffers(@Param("today") LocalDate today, @Param("status") OfferStatus status);

    // 14. Calculate average salary package
    @Query("SELECT AVG(o.salaryPackage) FROM Offer o")
    Double findAverageOfferedSalary();

    // 15. Update offer status
    @Modifying
    @Transactional
    @Query("""
            UPDATE Offer o
            SET o.status = :status
            WHERE o.id = :offerId
            """)
    int updateOfferStatus(
            @Param("offerId") Long offerId,
            @Param("status") OfferStatus status);

    // 16. Find offers belonging to a student
    Page<Offer> findByApplication_Student_Id(
            Long studentId, Pageable pageable);
}

package com.kshem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

import com.kshem.enums.Department;
import com.kshem.enums.DriveStatus;
import com.kshem.enums.EmploymentType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_drives")
@Data
@NoArgsConstructor
public class JobDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String jobTitle;

    @Column(length = 2000)
    private String jobDescription;

    @Column(nullable = false)
    private Double minimumCgpa;

    @ElementCollection(targetClass = Department.class, fetch = FetchType.LAZY)
    @CollectionTable(
        name = "job_drive_departments",
        joinColumns = @JoinColumn(name = "job_drive_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false)
    private Set<Department> eligibleDepartments = new HashSet<>();

    @Column(nullable = false)
    private Integer graduationYear;

    @Column(nullable = false)
    private Integer maximumBacklogs;

    @Column(nullable = false)
    private BigDecimal salaryPackage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DriveStatus status = DriveStatus.UPCOMING;

    private LocalDate applicationStartDate;

    private LocalDate applicationEndDate;

    private LocalDate driveDate;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}


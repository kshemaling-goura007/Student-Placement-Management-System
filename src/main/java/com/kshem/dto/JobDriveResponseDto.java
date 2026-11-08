package com.kshem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.kshem.enums.Department;
import com.kshem.enums.DriveStatus;
import com.kshem.enums.EmploymentType;

import lombok.Data;

@Data
public class JobDriveResponseDto {

    private Long id;

    private String jobTitle;

    private String jobDescription;

    private Double minimumCgpa;

    private Set<Department> eligibleDepartments;

    private Integer graduationYear;

    private Integer maximumBacklogs;

    private BigDecimal salaryPackage;

    private EmploymentType employmentType;

    private DriveStatus status;

    private LocalDate applicationStartDate;

    private LocalDate applicationEndDate;

    private LocalDate driveDate;

    private Long companyId;

    private String companyName;

    private LocalDateTime createdAt;
}

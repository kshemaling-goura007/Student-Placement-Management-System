package com.kshem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.kshem.enums.Department;
import com.kshem.enums.EmploymentType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class JobDriveRequestDto {

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 150, message = "Job title must be between 3 and 150 characters")
    private String jobTitle;

    @NotBlank(message = "Job description is required")
    @Size(max = 3000, message = "Job description cannot exceed 3000 characters")
    private String jobDescription;

    @NotNull(message = "Minimum CGPA is required")
    @DecimalMin(value = "0.0", message = "Minimum CGPA cannot be negative")
    private Double minimumCgpa;

    @NotEmpty(message = "At least one eligible department is required")
    private Set<Department> eligibleDepartments;

    @NotNull(message = "Graduation year is required")
    @Min(value = 2020, message = "Invalid graduation year")
    private Integer graduationYear;

    @NotNull(message = "Maximum backlogs is required")
    @PositiveOrZero(message = "Maximum backlogs cannot be negative")
    private Integer maximumBacklogs;

    @NotNull(message = "Salary package is required")
    @DecimalMin(value = "0.01", message = "Salary package must be positive")
    private BigDecimal salaryPackage;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Application start date is required")
    @FutureOrPresent(message = "Application start date cannot be in the past")
    private LocalDate applicationStartDate;

    @NotNull(message = "Application end date is required")
    private LocalDate applicationEndDate;

    @NotNull(message = "Drive date is required")
    private LocalDate driveDate;

}

package com.kshem.dto;

import java.time.LocalDate;

import com.kshem.enums.Department;

import lombok.Data;

@Data
public class StudentResponseDto {

    private Long id;

    private String fullName;

    private String registrationNumber;

    private String email;

    private String phoneNumber;

    private Department department;

    private Double cgpa;

    private Integer graduationYear;

    private Integer activeBacklogs;

    private Boolean placementEligible;

    private LocalDate dateOfBirth;

    private String resumeUrl;

}

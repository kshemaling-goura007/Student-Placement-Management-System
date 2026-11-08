package com.kshem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kshem.enums.OfferStatus;

import lombok.Data;

@Data
public class OfferResponseDto {

    private Long id;

    private BigDecimal salaryPackage;

    private String jobDesignation;

    private LocalDate offerDate;

    private LocalDate joiningDate;

    private LocalDate offerExpiryDate;

    private OfferStatus status;

    private String remarks;

    private LocalDateTime createdAt;

    private Long applicationId;

    private String studentName;

    private String companyName;

    private String jobTitle;
}

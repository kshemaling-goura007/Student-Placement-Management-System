package com.kshem.dto;

import com.kshem.enums.ApplicationStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplicationStatusUpdateDto {

    @NotNull(message = "Application status is required")
    private ApplicationStatus status;

    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters")
    private String remarks;
    
}
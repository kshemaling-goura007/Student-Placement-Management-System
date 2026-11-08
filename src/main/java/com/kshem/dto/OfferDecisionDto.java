package com.kshem.dto;

import com.kshem.enums.OfferStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OfferDecisionDto {

    @NotNull(message = "Offer decision is required")
    private OfferStatus status;

    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters")
    private String remarks;
}

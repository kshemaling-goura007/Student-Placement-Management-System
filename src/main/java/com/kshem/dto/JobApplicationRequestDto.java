package com.kshem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JobApplicationRequestDto {

    @NotNull(message = "Job drive ID is required")
    private Long jobDriveId;

    @Size(max = 1000, message = "Cover letter cannot exceed 1000 characters")
    private String coverLetter;
}

package com.kshem.dto;

import com.kshem.enums.InterviewResult;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InterviewResultUpdateDto {

    @NotNull(message = "Interview result is required")
    private InterviewResult result;

    @Size(max = 2000, message = "Feedback cannot exceed 2000 characters")
    private String feedback;
}
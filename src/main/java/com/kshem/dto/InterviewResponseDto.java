package com.kshem.dto;

import java.time.LocalDateTime;
import com.kshem.enums.InterviewResult;
import com.kshem.enums.InterviewStatus;

import lombok.Data;

@Data
public class InterviewResponseDto {

    private Long id;

    private String roundName;

    private String description;

    private LocalDateTime interviewDate;

    private String interviewerName;

    private String meetingLink;

    private InterviewStatus status;

    private InterviewResult result;

    private String feedback;

    private Long applicationId;

    private String studentName;

    private String jobTitle;

    private String companyName;
}
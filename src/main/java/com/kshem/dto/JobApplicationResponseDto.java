package com.kshem.dto;

import java.time.LocalDateTime;
import com.kshem.enums.ApplicationStatus;
import lombok.Data;

@Data
public class JobApplicationResponseDto {

    private Long id;

    private LocalDateTime appliedAt;

    private ApplicationStatus status;

    private String coverLetter;

    private String remarks;

    private Long studentId;

    private String studentName;

    private String registrationNumber;

    private Long jobDriveId;

    private String jobTitle;

    private String companyName;
}

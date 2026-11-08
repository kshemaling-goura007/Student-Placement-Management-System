package com.kshem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kshem.dto.ApplicationStatusUpdateDto;
import com.kshem.dto.JobApplicationRequestDto;
import com.kshem.dto.JobApplicationResponseDto;

public interface JobApplicationService {

    // Student applies for a job drive
    JobApplicationResponseDto applyForDrive(
            JobApplicationRequestDto dto);

    // Get application by ID
    JobApplicationResponseDto getApplicationById(Long applicationId);

    // Get all applications with pagination
    Page<JobApplicationResponseDto> getAllApplications(Pageable pageable);

    // Get applications submitted by a student
    Page<JobApplicationResponseDto> getApplicationsByStudent(Long studentId, Pageable pageable);

    // Get applications received for a job drive
    Page<JobApplicationResponseDto> getApplicationsByDrive(Long driveId, Pageable pageable);

    // Update application status
    JobApplicationResponseDto updateApplicationStatus(Long applicationId, ApplicationStatusUpdateDto dto);

    // Withdraw an application
    void withdrawApplication(Long applicationId);
}
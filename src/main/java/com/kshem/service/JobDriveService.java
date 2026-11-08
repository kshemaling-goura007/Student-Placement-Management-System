package com.kshem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kshem.dto.JobDriveRequestDto;
import com.kshem.dto.JobDriveResponseDto;

public interface JobDriveService {

    // Publish a new job drive
    JobDriveResponseDto publishDrive(JobDriveRequestDto dto);

    // Update existing job drive
    JobDriveResponseDto updateDrive(Long driveId, JobDriveRequestDto dto);

    // Get job drive by ID
    JobDriveResponseDto getDriveById(Long driveId);

    // Get all job drives with pagination
    Page<JobDriveResponseDto> getAllDrives(Pageable pageable);

    // Get active job drives
    Page<JobDriveResponseDto> getActiveDrives(Pageable pageable);

    // Get job drives published by a company
    Page<JobDriveResponseDto> getDrivesByCompany(Long companyId, Pageable pageable);

    // Check student eligibility for a particular drive
    boolean isStudentEligible(Long studentId, Long driveId);

    // Close a job drive
    void closeDrive(Long driveId);
}

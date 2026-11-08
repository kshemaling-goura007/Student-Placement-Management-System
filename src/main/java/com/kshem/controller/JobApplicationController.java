package com.kshem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kshem.dto.ApplicationStatusUpdateDto;
import com.kshem.dto.JobApplicationRequestDto;
import com.kshem.dto.JobApplicationResponseDto;
import com.kshem.service.JobApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService applicationService;

    // Apply for a job drive
    @PostMapping
    public ResponseEntity<JobApplicationResponseDto> applyForDrive(@Valid @RequestBody JobApplicationRequestDto dto) {

        return new ResponseEntity<>(applicationService.applyForDrive(dto), HttpStatus.CREATED);
    }

    // Get application by ID
    @GetMapping("/{applicationId}")
    public ResponseEntity<JobApplicationResponseDto> getApplicationById(
            @PathVariable Long applicationId) {

        return ResponseEntity.ok(applicationService.getApplicationById(applicationId));
    }

    // Get all applications
    @GetMapping
    public ResponseEntity<Page<JobApplicationResponseDto>> getAllApplications(@PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(applicationService.getAllApplications(pageable));
    }

    // Get applications by student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<JobApplicationResponseDto>> getApplicationsByStudent(@PathVariable Long studentId, @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(applicationService.getApplicationsByStudent(studentId, pageable));
    }

    // Get applications by job drive
    @GetMapping("/drive/{driveId}")
    public ResponseEntity<Page<JobApplicationResponseDto>> getApplicationsByDrive(@PathVariable Long driveId, @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(applicationService.getApplicationsByDrive(driveId, pageable));
    }

    // Update application status
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<JobApplicationResponseDto> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateDto dto) {

        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, dto));
    }

    // Withdraw application
    @PatchMapping("/{applicationId}/withdraw")
    public ResponseEntity<Void> withdrawApplication(@PathVariable Long applicationId) {

        applicationService.withdrawApplication(applicationId);

        return ResponseEntity.noContent().build();
    }
}

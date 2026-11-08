package com.kshem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kshem.dto.JobDriveRequestDto;
import com.kshem.dto.JobDriveResponseDto;
import com.kshem.service.JobDriveService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/job-drives")
@RequiredArgsConstructor
public class JobDriveController {

    private final JobDriveService jobDriveService;

    // Publish job drive
    @PostMapping
    public ResponseEntity<JobDriveResponseDto> publishDrive(@Valid @RequestBody JobDriveRequestDto dto) {

        return new ResponseEntity<>(jobDriveService.publishDrive(dto), HttpStatus.CREATED);
    }

    // Update job drive
    @PutMapping("/{driveId}")
    public ResponseEntity<JobDriveResponseDto> updateDrive(@PathVariable Long driveId, @Valid @RequestBody JobDriveRequestDto dto) {

        return ResponseEntity.ok(jobDriveService.updateDrive(driveId, dto));
    }

    // Get job drive by ID
    @GetMapping("/{driveId}")
    public ResponseEntity<JobDriveResponseDto> getDriveById(@PathVariable Long driveId) {

        return ResponseEntity.ok(jobDriveService.getDriveById(driveId));
    }

    // Get all job drives
    @GetMapping
    public ResponseEntity<Page<JobDriveResponseDto>> getAllDrives(@PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(jobDriveService.getAllDrives(pageable));
    }

    // Get active job drives
    @GetMapping("/active")
    public ResponseEntity<Page<JobDriveResponseDto>> getActiveDrives(@PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(jobDriveService.getActiveDrives(pageable));
    }

    // Get drives by company
    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<JobDriveResponseDto>> getDrivesByCompany(@PathVariable Long companyId, @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(jobDriveService.getDrivesByCompany(companyId, pageable));
    }

    // Check student eligibility
    @GetMapping("/{driveId}/eligibility/{studentId}")
    public ResponseEntity<Boolean> checkEligibility(@PathVariable Long driveId, @PathVariable Long studentId) {

        return ResponseEntity.ok(jobDriveService.isStudentEligible(studentId, driveId));
    }

    // Close job drive
    @PatchMapping("/{driveId}/close")
    public ResponseEntity<Void> closeDrive(@PathVariable Long driveId) {

        jobDriveService.closeDrive(driveId);

        return ResponseEntity.noContent().build();
    }
}
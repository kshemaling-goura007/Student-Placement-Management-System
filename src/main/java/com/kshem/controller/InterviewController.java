package com.kshem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kshem.dto.InterviewRequestDto;
import com.kshem.dto.InterviewResponseDto;
import com.kshem.dto.InterviewResultUpdateDto;
import com.kshem.service.InterviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    // Schedule interview
    @PostMapping
    public ResponseEntity<InterviewResponseDto> scheduleInterview(
            @Valid @RequestBody InterviewRequestDto dto) {

        return new ResponseEntity<>(
                interviewService.scheduleInterview(dto),
                HttpStatus.CREATED);
    }

    // Get interview by ID
    @GetMapping("/{interviewId}")
    public ResponseEntity<InterviewResponseDto> getInterviewById(
            @PathVariable Long interviewId) {

        return ResponseEntity.ok(
                interviewService.getInterviewById(interviewId));
    }

    // Get all interviews
    @GetMapping
    public ResponseEntity<Page<InterviewResponseDto>> getAllInterviews(
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(interviewService.getAllInterviews(pageable));
    }

    // Get interviews by application
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<Page<InterviewResponseDto>> getInterviewsByApplication(
            @PathVariable Long applicationId,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(interviewService.getInterviewsByApplication(applicationId, pageable));
    }

    // Update interview
    @PutMapping("/{interviewId}")
    public ResponseEntity<InterviewResponseDto> updateInterview(
            @PathVariable Long interviewId,
            @Valid @RequestBody InterviewRequestDto dto) {

        return ResponseEntity.ok(interviewService.updateInterview(interviewId, dto));
    }

    // Update interview result
    @PatchMapping("/{interviewId}/result")
    public ResponseEntity<InterviewResponseDto> updateInterviewResult(
            @PathVariable Long interviewId,
            @Valid @RequestBody InterviewResultUpdateDto dto) {

        return ResponseEntity.ok(interviewService.updateInterviewResult(interviewId, dto));
    }

    // Cancel interview
    @PatchMapping("/{interviewId}/cancel")
    public ResponseEntity<Void> cancelInterview(
            @PathVariable Long interviewId) {

        interviewService.cancelInterview(interviewId);

        return ResponseEntity.noContent().build();
    }
}

package com.kshem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kshem.dto.InterviewRequestDto;
import com.kshem.dto.InterviewResponseDto;
import com.kshem.dto.InterviewResultUpdateDto;

public interface InterviewService {

    // Schedule a new interview
    InterviewResponseDto scheduleInterview(InterviewRequestDto dto);

    // Get interview by ID
    InterviewResponseDto getInterviewById(Long interviewId);

    // Get all interviews with pagination
    Page<InterviewResponseDto> getAllInterviews(Pageable pageable);

    // Get interviews for a particular application
    Page<InterviewResponseDto> getInterviewsByApplication(Long applicationId, Pageable pageable);

    // Update interview details or reschedule
    InterviewResponseDto updateInterview(Long interviewId, InterviewRequestDto dto);

    // Update interview result and feedback
    InterviewResponseDto updateInterviewResult(Long interviewId, InterviewResultUpdateDto dto);

    // Cancel an interview
    void cancelInterview(Long interviewId);
}

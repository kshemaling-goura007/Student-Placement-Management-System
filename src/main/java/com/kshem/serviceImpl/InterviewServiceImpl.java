package com.kshem.serviceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kshem.dto.InterviewRequestDto;
import com.kshem.dto.InterviewResponseDto;
import com.kshem.dto.InterviewResultUpdateDto;
import com.kshem.entity.Interview;
import com.kshem.entity.JobApplication;
import com.kshem.enums.ApplicationStatus;
import com.kshem.enums.InterviewResult;
import com.kshem.enums.InterviewStatus;
import com.kshem.exception.ApplicationNotFoundException;
import com.kshem.exception.InterviewNotFoundException;
import com.kshem.mapper.InterviewMapper;
import com.kshem.repository.InterviewRepository;
import com.kshem.repository.JobApplicationRepository;
import com.kshem.service.InterviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository applicationRepository;
    private final InterviewMapper interviewMapper;

    // 1. Schedule Interview
    @Override
    public InterviewResponseDto scheduleInterview(InterviewRequestDto dto) {

        checkPlacementOfficer();

        JobApplication application = applicationRepository
                .findById(dto.getApplicationId())
                .orElseThrow(() -> new ApplicationNotFoundException(
                        "Application not found with ID: " + dto.getApplicationId()));

        if (application.getStatus() != ApplicationStatus.SHORTLISTED) {
            throw new IllegalArgumentException(
                    "Interview can only be scheduled for shortlisted applications");
        }

        Interview interview = interviewMapper.toEntity(dto);

        // Correct relationship field name
        interview.setApplication(application);

        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setResult(InterviewResult.PENDING);

        Interview savedInterview = interviewRepository.save(interview);

        return interviewMapper.toResponseDto(savedInterview);
    }

    // 2. Get Interview By ID
    @Override
    @Transactional(readOnly = true)
    public InterviewResponseDto getInterviewById(Long interviewId) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(
                        "Interview not found with ID: " + interviewId));

        return interviewMapper.toResponseDto(interview);
    }

    // 3. Get All Interviews
    @Override
    @Transactional(readOnly = true)
    public Page<InterviewResponseDto> getAllInterviews(Pageable pageable) {

        return interviewRepository.findAll(pageable)
                .map(interviewMapper::toResponseDto);
    }

    // 4. Get Interviews By Application
    @Override
    @Transactional(readOnly = true)
    public Page<InterviewResponseDto> getInterviewsByApplication(
            Long applicationId, Pageable pageable) {

        if (!applicationRepository.existsById(applicationId)) {
            throw new ApplicationNotFoundException(
                    "Application not found with ID: " + applicationId);
        }

        return interviewRepository
                .findByApplication_Id(applicationId, pageable)
                .map(interviewMapper::toResponseDto);
    }

    // 5. Update Interview
    @Override
    public InterviewResponseDto updateInterview(
            Long interviewId, InterviewRequestDto dto) {

        checkPlacementOfficer();

        Interview interview = getInterviewEntity(interviewId);

        // Update interview details
        interview.setRoundName(dto.getRoundName());
        interview.setDescription(dto.getDescription());
        interview.setInterviewDate(dto.getInterviewDate());
        interview.setInterviewerName(dto.getInterviewerName());
        interview.setMeetingLink(dto.getMeetingLink());

        Interview updatedInterview = interviewRepository.save(interview);

        return interviewMapper.toResponseDto(updatedInterview);
    }

    // 6. Update Interview Result
    @Override
    public InterviewResponseDto updateInterviewResult(
            Long interviewId, InterviewResultUpdateDto dto) {

        checkPlacementOfficer();

        Interview interview = getInterviewEntity(interviewId);

        interviewMapper.updateResult(dto, interview);

        interview.setStatus(InterviewStatus.COMPLETED);

        Interview updatedInterview = interviewRepository.save(interview);

        return interviewMapper.toResponseDto(updatedInterview);
    }

    // 7. Cancel Interview
    @Override
    public void cancelInterview(Long interviewId) {

        checkPlacementOfficer();

        Interview interview = getInterviewEntity(interviewId);

        if (interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Completed interview cannot be cancelled");
        }

        interview.setStatus(InterviewStatus.CANCELLED);

        interviewRepository.save(interview);
    }

    // Helper: Get Interview Entity
    private Interview getInterviewEntity(Long interviewId) {

        return interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(
                        "Interview not found with ID: " + interviewId));
    }

    // Helper: Check Placement Officer or Admin
    private void checkPlacementOfficer() {

        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Authentication required");
        }

        List<String> allowedRoles = List.of(
                "ROLE_ADMIN",
                "ROLE_PLACEMENT_OFFICER");

        boolean hasPermission = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        allowedRoles.contains(authority.getAuthority()));

        if (!hasPermission) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Only Admin or Placement Officer can perform this operation");
        }
    }
}
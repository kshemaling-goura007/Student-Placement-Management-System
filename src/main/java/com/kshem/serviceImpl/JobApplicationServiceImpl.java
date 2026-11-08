package com.kshem.serviceImpl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kshem.dto.ApplicationStatusUpdateDto;
import com.kshem.dto.JobApplicationRequestDto;
import com.kshem.dto.JobApplicationResponseDto;
import com.kshem.entity.JobApplication;
import com.kshem.entity.JobDrive;
import com.kshem.entity.Student;
import com.kshem.enums.ApplicationStatus;
import com.kshem.enums.DriveStatus;
import com.kshem.mapper.JobApplicationMapper;
import com.kshem.repository.JobApplicationRepository;
import com.kshem.repository.JobDriveRepository;
import com.kshem.repository.StudentRepository;
import com.kshem.service.JobApplicationService;
import com.kshem.service.JobDriveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final JobDriveRepository jobDriveRepository;
    private final JobApplicationMapper applicationMapper;
    private final JobDriveService jobDriveService;

    // Apply for a job drive
    @Override
    public JobApplicationResponseDto applyForDrive(
            JobApplicationRequestDto dto) {

        String email = getLoggedInEmail();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        JobDrive jobDrive = jobDriveRepository.findById(dto.getJobDriveId())
                .orElseThrow(() -> new RuntimeException("Job drive not found"));

        // Correct repository method name
        if (applicationRepository.existsByStudent_IdAndJobDrive_Id(
                student.getId(), jobDrive.getId())) {

            throw new RuntimeException("You have already applied for this job drive");
        }

        if (jobDrive.getStatus() != DriveStatus.ACTIVE) {
            throw new RuntimeException("Applications are not open for this job drive");
        }

        LocalDate today = LocalDate.now();

        if (today.isBefore(jobDrive.getApplicationStartDate())
                || today.isAfter(jobDrive.getApplicationEndDate())) {

            throw new RuntimeException("Job drive application period is closed");
        }

        boolean eligible = jobDriveService.isStudentEligible(
                student.getId(), jobDrive.getId());

        if (!eligible) {
            throw new RuntimeException("You are not eligible for this job drive");
        }

        JobApplication application = applicationMapper.toEntity(dto);

        application.setStudent(student);
        application.setJobDrive(jobDrive);
        application.setStatus(ApplicationStatus.APPLIED);

        // Set applied date if it is not automatically set in the entity
        if (application.getAppliedAt() == null) {
            application.setAppliedAt(java.time.LocalDateTime.now());
        }

        JobApplication savedApplication = applicationRepository.save(application);

        return applicationMapper.toResponseDto(savedApplication);
    }

    // Get application by ID
    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponseDto getApplicationById(
            Long applicationId) {

        JobApplication application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        return applicationMapper.toResponseDto(application);
    }

    // Get all applications
    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponseDto> getAllApplications(
            Pageable pageable) {

        return applicationRepository.findAll(pageable)
                .map(applicationMapper::toResponseDto);
    }

    // Get applications by student
    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponseDto> getApplicationsByStudent(
            Long studentId, Pageable pageable) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        String email = getLoggedInEmail();

        if (!student.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You cannot view another student's applications");
        }

        // Correct repository method name
        return applicationRepository
                .findByStudent_Id(studentId, pageable)
                .map(applicationMapper::toResponseDto);
    }

    // Get applications by job drive
    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponseDto> getApplicationsByDrive(Long driveId, Pageable pageable) {

        jobDriveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Job drive not found"));

        // Correct repository method name
        return applicationRepository
                .findByJobDrive_Id(driveId, pageable)
                .map(applicationMapper::toResponseDto);
    }

    // Update application status
    @Override
    public JobApplicationResponseDto updateApplicationStatus(
            Long applicationId, ApplicationStatusUpdateDto dto) {

        JobApplication application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!hasAuthority("ROLE_PLACEMENT_OFFICER") && !hasAuthority("ROLE_ADMIN")) {

            throw new RuntimeException("You are not authorized to update application status");
        }

        ApplicationStatus currentStatus = application.getStatus();
        ApplicationStatus newStatus = dto.getStatus();

        if (newStatus == null
                || newStatus == ApplicationStatus.APPLIED
                || newStatus == ApplicationStatus.WITHDRAWN) {

            throw new RuntimeException("Invalid application status update");
        }

        if (currentStatus == ApplicationStatus.REJECTED
                || currentStatus == ApplicationStatus.WITHDRAWN
                || currentStatus == ApplicationStatus.SELECTED) {

            throw new RuntimeException("Application status cannot be changed further");
        }

        application.setStatus(newStatus);
        application.setRemarks(dto.getRemarks());

        JobApplication updatedApplication = applicationRepository.save(application);

        return applicationMapper.toResponseDto(updatedApplication);
    }

    // Withdraw application
    @Override
    public void withdrawApplication(Long applicationId) {

        JobApplication application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        String email = getLoggedInEmail();

        if (!application.getStudent().getEmail().equalsIgnoreCase(email)) {

            throw new RuntimeException("You cannot withdraw another student's application");
        }

        if (application.getStatus() == ApplicationStatus.SELECTED
                || application.getStatus() == ApplicationStatus.REJECTED
                || application.getStatus() == ApplicationStatus.WITHDRAWN) {

            throw new RuntimeException("This application cannot be withdrawn");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        applicationRepository.save(application);
    }

    // Get logged-in user's email
    private String getLoggedInEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    // Check user authority
    private boolean hasAuthority(String role) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }
}
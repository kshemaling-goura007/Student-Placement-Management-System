package com.kshem.serviceImpl;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kshem.dto.JobDriveRequestDto;
import com.kshem.dto.JobDriveResponseDto;
import com.kshem.entity.Company;
import com.kshem.entity.JobDrive;
import com.kshem.entity.Student;
import com.kshem.entity.User;
import com.kshem.enums.DriveStatus;
import com.kshem.mapper.JobDriveMapper;
import com.kshem.repository.CompanyRepository;
import com.kshem.repository.JobDriveRepository;
import com.kshem.repository.StudentRepository;
import com.kshem.repository.UserRepository;
import com.kshem.service.JobDriveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class JobDriveServiceImpl implements JobDriveService {

    private final JobDriveRepository jobDriveRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final JobDriveMapper jobDriveMapper;

    // Publish Job Drive
    @Override
    public JobDriveResponseDto publishDrive(JobDriveRequestDto dto) {

        validateDriveDates(dto);

        String email = getLoggedInEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Company profile not found"));

        JobDrive jobDrive = jobDriveMapper.toEntity(dto);

        jobDrive.setCompany(company);
        jobDrive.setStatus(DriveStatus.UPCOMING);

        JobDrive savedDrive = jobDriveRepository.save(jobDrive);

        return jobDriveMapper.toResponseDto(savedDrive);
    }

    // Update Job Drive
    @Override
    public JobDriveResponseDto updateDrive(Long driveId, JobDriveRequestDto dto) {

        validateDriveDates(dto);

        JobDrive jobDrive = jobDriveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Job drive not found"));

        verifyCompanyOwnership(jobDrive);

        if (jobDrive.getStatus() == DriveStatus.CLOSED ||
            jobDrive.getStatus() == DriveStatus.CANCELLED) {

            throw new RuntimeException("Closed or cancelled drives cannot be updated");
        }

        jobDrive.setJobTitle(dto.getJobTitle());
        jobDrive.setJobDescription(dto.getJobDescription());
        jobDrive.setMinimumCgpa(dto.getMinimumCgpa());
        jobDrive.setEligibleDepartments(dto.getEligibleDepartments());
        jobDrive.setGraduationYear(dto.getGraduationYear());
        jobDrive.setMaximumBacklogs(dto.getMaximumBacklogs());
        jobDrive.setSalaryPackage(dto.getSalaryPackage());
        jobDrive.setEmploymentType(dto.getEmploymentType());
        jobDrive.setApplicationStartDate(dto.getApplicationStartDate());
        jobDrive.setApplicationEndDate(dto.getApplicationEndDate());
        jobDrive.setDriveDate(dto.getDriveDate());

        JobDrive updatedDrive = jobDriveRepository.save(jobDrive);

        return jobDriveMapper.toResponseDto(updatedDrive);
    }

    // Get Job Drive By ID
    @Override
    @Transactional(readOnly = true)
    public JobDriveResponseDto getDriveById(Long driveId) {

        JobDrive jobDrive = jobDriveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Job drive not found"));

        return jobDriveMapper.toResponseDto(jobDrive);
    }

    // Get All Job Drives With Pagination
    @Override
    @Transactional(readOnly = true)
    public Page<JobDriveResponseDto> getAllDrives(Pageable pageable) {

        return jobDriveRepository.findAll(pageable)
                .map(jobDriveMapper::toResponseDto);
    }

    // Get Active Job Drives
    @Override
    @Transactional(readOnly = true)
    public Page<JobDriveResponseDto> getActiveDrives(Pageable pageable) {

        return jobDriveRepository.findByStatus(DriveStatus.ACTIVE, pageable)
                .map(jobDriveMapper::toResponseDto);
    }

    // Get Drives By Company
    @Override
    @Transactional(readOnly = true)
    public Page<JobDriveResponseDto> getDrivesByCompany(Long companyId, Pageable pageable) {

        return jobDriveRepository.findByCompanyId(companyId, pageable)
                .map(jobDriveMapper::toResponseDto);
    }

    // Check Student Eligibility
    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEligible(Long studentId, Long driveId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        JobDrive jobDrive = jobDriveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Job drive not found"));

        LocalDate today = LocalDate.now();

        // Check general placement eligibility
        if (!student.isPlacementEligible()) {
            return false;
        }

        // Check minimum CGPA
        if (student.getCgpa() < jobDrive.getMinimumCgpa()) {
            return false;
        }

        // Check department eligibility
        if (jobDrive.getEligibleDepartments() != null &&
            !jobDrive.getEligibleDepartments().isEmpty() &&
            !jobDrive.getEligibleDepartments()
                    .contains(student.getDepartment())) {
            return false;
        }

        // Check graduation year
        if (!Objects.equals(student.getGraduationYear(), jobDrive.getGraduationYear())) {
            return false;
        }

        // Check active backlogs
        if (student.getActiveBacklogs() > jobDrive.getMaximumBacklogs()) {
            return false;
        }

        // Check drive status
        if (jobDrive.getStatus() != DriveStatus.ACTIVE) {
            return false;
        }

        // Check application period
        if (today.isBefore(jobDrive.getApplicationStartDate()) ||
            today.isAfter(jobDrive.getApplicationEndDate())) {
            return false;
        }

        return true;
    }

    // Close Job Drive
    @Override
    public void closeDrive(Long driveId) {

        JobDrive jobDrive = jobDriveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Job drive not found"));

        verifyCompanyOwnership(jobDrive);

        if (jobDrive.getStatus() == DriveStatus.CANCELLED) {
            throw new RuntimeException("Cancelled drive cannot be closed");
        }

        jobDrive.setStatus(DriveStatus.CLOSED);

        jobDriveRepository.save(jobDrive);
    }

    // Get Logged-in User Email
    private String getLoggedInEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    // Verify Company Ownership
    private void verifyCompanyOwnership(JobDrive jobDrive) {

        String email = getLoggedInEmail();

        if (!jobDrive.getCompany().getUser().getEmail().equalsIgnoreCase(email)) {

            throw new RuntimeException("You cannot modify another company's job drive");
        }
    }

    // Validate Application Dates
    private void validateDriveDates(JobDriveRequestDto dto) {

        if (dto.getApplicationStartDate() == null ||
            dto.getApplicationEndDate() == null ||
            dto.getDriveDate() == null) {

            throw new RuntimeException("Application dates and drive date are required");
        }

        if (!dto.getApplicationEndDate().isAfter(dto.getApplicationStartDate())) {

            throw new RuntimeException("Application end date must be after start date");
        }

        if (!dto.getDriveDate().isAfter(dto.getApplicationEndDate())) {

            throw new RuntimeException("Drive date must be after application end date");
        }
    }
}
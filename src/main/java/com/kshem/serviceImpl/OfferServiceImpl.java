package com.kshem.serviceImpl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kshem.dto.OfferDecisionDto;
import com.kshem.dto.OfferResponseDto;
import com.kshem.entity.JobApplication;
import com.kshem.entity.Offer;
import com.kshem.entity.Student;
import com.kshem.enums.ApplicationStatus;
import com.kshem.enums.OfferStatus;
import com.kshem.mapper.OfferMapper;
import com.kshem.repository.JobApplicationRepository;
import com.kshem.repository.OfferRepository;
import com.kshem.repository.StudentRepository;
import com.kshem.service.OfferService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final JobApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final OfferMapper offerMapper;

    // 1. Create Offer
    @Override
    public OfferResponseDto createOffer(Long applicationId) {

        checkPlacementOfficer();

        JobApplication application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                    new RuntimeException("Application not found"));

        if (application.getStatus() != ApplicationStatus.SELECTED) {
            throw new RuntimeException("Offer can only be created for selected applications");
        }

        if (offerRepository.findByApplication_Id(applicationId).isPresent()) {
            throw new RuntimeException("Offer already exists for this application");
        }

        Offer offer = new Offer();

        offer.setApplication(application);
        offer.setSalaryPackage(application.getJobDrive().getSalaryPackage());
        offer.setJobDesignation(application.getJobDrive().getJobTitle());

        offer.setOfferDate(LocalDate.now());

        // Example expiry policy: 7 days
        offer.setOfferExpiryDate(LocalDate.now().plusDays(7));

        offer.setStatus(OfferStatus.OFFERED);

        Offer savedOffer = offerRepository.save(offer);

        return offerMapper.toResponseDto(savedOffer);
    }

    // 2. Get Offer By ID
    @Override
    @Transactional(readOnly = true)
    public OfferResponseDto getOfferById(Long offerId) {

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() ->
                    new RuntimeException("Offer not found"));

        return offerMapper.toResponseDto(offer);
    }

    // 3. Get All Offers
    @Override
    @Transactional(readOnly = true)
    public Page<OfferResponseDto> getAllOffers(Pageable pageable) {

        return offerRepository.findAll(pageable)
                .map(offerMapper::toResponseDto);
    }

    // 4. Get Offers By Student
    @Override
    @Transactional(readOnly = true)
    public Page<OfferResponseDto> getOffersByStudent(
            Long studentId, Pageable pageable) {

        String email = getLoggedInEmail();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                    new RuntimeException("Student not found"));

        if (!student.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You are not authorized to view these offers");
        }

        return offerRepository
                .findByApplication_Student_Id(studentId, pageable)
                .map(offerMapper::toResponseDto);
    }

    
    // 5. Get Offer By Application ID
    @Override
    @Transactional(readOnly = true)
    public OfferResponseDto getOfferByApplication(Long applicationId) {

        Offer offer = offerRepository
                .findByApplication_Id(applicationId)
                .orElseThrow(() ->
                    new RuntimeException("Offer not found"));

        return offerMapper.toResponseDto(offer);
    }

    // 6. Accept Or Reject Offer
    @Override
    public OfferResponseDto updateOfferDecision(
            Long offerId, OfferDecisionDto dto) {

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() ->
                    new RuntimeException("Offer not found"));

        String email = getLoggedInEmail();

        String studentEmail = offer.getApplication()
                .getStudent().getEmail();

        if (!studentEmail.equalsIgnoreCase(email)) {
            throw new RuntimeException("You are not authorized to update this offer");
        }

        if (offer.getStatus() != OfferStatus.OFFERED) {
            throw new RuntimeException("Only offered offers can be accepted or rejected");
        }

        if (dto.getStatus() != OfferStatus.ACCEPTED
                && dto.getStatus() != OfferStatus.REJECTED) {

            throw new RuntimeException("Offer decision must be ACCEPTED or REJECTED");
        }

        if (LocalDate.now().isAfter(offer.getOfferExpiryDate())) {

            offer.setStatus(OfferStatus.EXPIRED);

            Offer expiredOffer = offerRepository.save(offer);

            return offerMapper.toResponseDto(expiredOffer);
        }

        offerMapper.updateDecision(dto, offer);

        Offer updatedOffer = offerRepository.save(offer);

        return offerMapper.toResponseDto(updatedOffer);
    }

    // 7. Expire Offer
    @Override
    public void expireOffer(Long offerId) {

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        if (offer.getStatus() != OfferStatus.OFFERED) {
            throw new RuntimeException("Only pending offers can be expired");
        }

        if (!LocalDate.now().isAfter(offer.getOfferExpiryDate())) {
            throw new RuntimeException("Offer expiry date has not passed yet");
        }

        offer.setStatus(OfferStatus.EXPIRED);

        offerRepository.save(offer);
    }

    // Helper method: Get logged-in email
    private String getLoggedInEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getName();
    }

    // Helper method: Check placement officer or admin
    private void checkPlacementOfficer() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        boolean authorized = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_ADMIN")
                    || authority.getAuthority().equals("ROLE_PLACEMENT_OFFICER"));

        if (!authorized) {
            throw new RuntimeException("Only admin or placement officer can create offers");
        }
    }
}

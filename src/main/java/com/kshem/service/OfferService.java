package com.kshem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kshem.dto.OfferDecisionDto;
import com.kshem.dto.OfferResponseDto;

public interface OfferService {

    // Create an offer for a selected application
    OfferResponseDto createOffer(Long applicationId);

    // Get offer by ID
    OfferResponseDto getOfferById(Long offerId);

    // Get all offers with pagination
    Page<OfferResponseDto> getAllOffers(Pageable pageable);

    // Get offers belonging to a particular student
    Page<OfferResponseDto> getOffersByStudent(Long studentId, Pageable pageable);

    // Get offer associated with an application
    OfferResponseDto getOfferByApplication(Long applicationId);

    // Accept or reject an offer
    OfferResponseDto updateOfferDecision(Long offerId, OfferDecisionDto dto);

    // Expire an offer after its expiry date
    void expireOffer(Long offerId);
    
}
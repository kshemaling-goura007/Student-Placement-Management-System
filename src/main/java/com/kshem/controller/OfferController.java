package com.kshem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kshem.dto.OfferDecisionDto;
import com.kshem.dto.OfferResponseDto;
import com.kshem.service.OfferService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    // Create offer for selected application
    @PostMapping("/application/{applicationId}")
    public ResponseEntity<OfferResponseDto> createOffer(@PathVariable Long applicationId) {

        return new ResponseEntity<>(offerService.createOffer(applicationId), HttpStatus.CREATED);
    }

    // Get offer by ID
    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponseDto> getOfferById(@PathVariable Long offerId) {

        return ResponseEntity.ok(offerService.getOfferById(offerId));
    }

    // Get all offers
    @GetMapping
    public ResponseEntity<Page<OfferResponseDto>> getAllOffers(@PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(offerService.getAllOffers(pageable));
    }

    // Get offers by student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<OfferResponseDto>> getOffersByStudent(@PathVariable Long studentId, @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(offerService.getOffersByStudent(studentId, pageable));
    }

    // Get offer by application ID
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<OfferResponseDto> getOfferByApplication(@PathVariable Long applicationId) {

        return ResponseEntity.ok(offerService.getOfferByApplication(applicationId));
    }

    // Accept or reject offer
    @PatchMapping("/{offerId}/decision")
    public ResponseEntity<OfferResponseDto> updateOfferDecision(@PathVariable Long offerId, @Valid @RequestBody OfferDecisionDto dto) {

        return ResponseEntity.ok(offerService.updateOfferDecision(offerId, dto));
    }

    // Expire offer
    @PatchMapping("/{offerId}/expire")
    public ResponseEntity<Void> expireOffer(@PathVariable Long offerId) {

        offerService.expireOffer(offerId);

        return ResponseEntity.noContent().build();
    }
}
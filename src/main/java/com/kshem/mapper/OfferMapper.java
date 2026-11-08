package com.kshem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.kshem.dto.OfferDecisionDto;
import com.kshem.dto.OfferResponseDto;
import com.kshem.entity.Offer;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    // Entity to Response DTO
    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "studentName",
             source = "application.student.fullName")
    @Mapping(target = "companyName",
             source = "application.jobDrive.company.companyName")
    @Mapping(target = "jobTitle",
             source = "application.jobDrive.jobTitle")
    OfferResponseDto toResponseDto(Offer offer);

    // Update Offer Decision
    void updateDecision(OfferDecisionDto dto, @MappingTarget Offer offer);
}


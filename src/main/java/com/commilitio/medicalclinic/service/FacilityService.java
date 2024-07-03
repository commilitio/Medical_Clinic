package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.exception.FacilityException;
import com.commilitio.medicalclinic.mapper.FacilityMapper;
import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.model.FacilityDto;
import com.commilitio.medicalclinic.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    public FacilityDto getFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityException("Facility Not Found.", HttpStatus.NOT_FOUND));
        return facilityMapper.toDto(facility);
    }

    public List<FacilityDto> getFacilities(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Facility> facilities = facilityRepository.findAll(pageable);
        return facilityMapper.toDtos(facilities.toList());
    }

    @Transactional
    public FacilityDto addFacility(Facility facility) {
        boolean facilityExists = facilityRepository
                .findByName(facility.getName())
                .isPresent();
        if (facilityExists) {
            throw new FacilityException("Facility already exists.", HttpStatus.CONFLICT);
        }
        Facility addedFacility = facilityRepository.save(facility);
        log.info("Created facility with data: {}", facility);
        return facilityMapper.toDto(addedFacility);
    }

    public void deleteFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityException("Facility Not Found.", HttpStatus.NOT_FOUND));
        facilityRepository.delete(facility);
        log.info("Deleted facity with id: {}", id);
    }
}
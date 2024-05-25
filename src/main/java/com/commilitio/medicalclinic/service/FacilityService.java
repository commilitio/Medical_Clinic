package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.mapper.FacilityMapper;
import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.model.FacilityDto;
import com.commilitio.medicalclinic.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    public FacilityDto getFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility Not Found."));
        return facilityMapper.toDto(facility);
    }

    public List<FacilityDto> getFacilities() {
        List<Facility> facilities = facilityRepository.findAll();
        return facilityMapper.toDtos(facilities);
    }

    @Transactional
    public FacilityDto addFacility(Facility facility) {
        boolean facilityExists = facilityRepository
                .findByName(facility.getName())
                .isPresent();
        if (facilityExists) {
            throw new IllegalArgumentException("Facility already exists.");
        }
        Facility addedFacility = facilityRepository.save(facility);
        return facilityMapper.toDto(addedFacility);
    }

    public void deleteFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility Not Found."));
        facilityRepository.delete(facility);
    }
}
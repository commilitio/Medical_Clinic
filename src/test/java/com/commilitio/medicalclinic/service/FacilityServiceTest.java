package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.exception.FacilityException;
import com.commilitio.medicalclinic.mapper.FacilityMapper;
import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.model.FacilityDto;
import com.commilitio.medicalclinic.repository.FacilityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FacilityServiceTest {

    private FacilityService facilityService;
    private FacilityRepository facilityRepository;
    private FacilityMapper facilityMapper;

    @BeforeEach
    void setUp() {
        this.facilityRepository = Mockito.mock(FacilityRepository.class);
        this.facilityMapper = Mappers.getMapper(FacilityMapper.class);

        this.facilityService = new FacilityService(facilityRepository, facilityMapper);
    }

    @Test
    void getFacility_FacilityExist_ReturnFacility() {
        // given
        Facility facility = new Facility(1L, "Hosp", "Waw", "12345", "st Xyz", "16", new HashSet<>());
        when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility));
        // when
        FacilityDto result = facilityService.getFacility(1L);
        // then
        Assertions.assertEquals(facilityMapper.toDto(facility), result); //  << ok ?
    }

    @Test
    void getFacility_FacilityDoesNotExist_ThrowsIllegalArgumentException() {
        // given
        when(facilityRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(FacilityException.class, () -> facilityService.getFacility(1L));
        // then
        Assertions.assertEquals("Facility Not Found.", result.getMessage());
    }

    @Test
    void getFacilities_FacilitiesExist_ReturnFacilities() {
        // given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Facility facility1 = new Facility(1L, "Hosp1", "Waw", "12345", "st Xyz", "16", new HashSet<>());
        Facility facility2 = new Facility(2L, "Hosp2", "Waw", "12347", "st Xyz", "18", new HashSet<>());
        List<Facility> facilities = Arrays.asList(facility1, facility2);
        Page<Facility> facilityPage = new PageImpl<>(facilities, pageable, facilities.size());
        when(facilityRepository.findAll(pageable)).thenReturn(facilityPage);
        // when
        var result = facilityService.getFacilities(pageNumber, pageSize);
        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void addFacility_FacilityAlreadyExists_ThrowsIllegalArgumentException() {
        // given
        Facility facility = new Facility(1L, "Hosp", "Waw", "12345", "st Xyz", "16", new HashSet<>());
        when(facilityRepository.findByName("Hosp")).thenReturn(Optional.of(facility));
        // when
        Exception result = Assertions.assertThrows(FacilityException.class, () -> facilityService.addFacility(facility));
        // then
        Assertions.assertEquals("Facility already exists.", result.getMessage());
    }

    @Test
    void addFacility_CorrectData_ReturnFacility() {
        // given
        Facility facility = new Facility(1L, "Hosp", "Waw", "12345", "st Xyz", "16", new HashSet<>());
        when(facilityRepository.findByName("Hosp")).thenReturn(Optional.empty());
        when(facilityRepository.save(facility)).thenReturn(facility);
        // when
        FacilityDto result = facilityService.addFacility(facility);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Hosp", result.getName());
        Assertions.assertEquals("Waw", result.getCity());
        Assertions.assertEquals("12345", result.getZipCode());
        Assertions.assertEquals("st Xyz", result.getStreetName());
        Assertions.assertEquals("16", result.getStreetNumber());
    }

    @Test
    void deleteFacility_FacilityDoesNotExist_ThrowsIllegalArgumentException() {
        // given
        when(facilityRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(FacilityException.class, () -> facilityService.deleteFacility(1L));
        // then
        Assertions.assertEquals("Facility Not Found.", result.getMessage());
    }

    @Test
    void deleteFacility_CorrectData_FacilityDeleted() {
        // given
        Facility facility = new Facility(1L, "Hosp", "Waw", "12345", "st Xyz", "16", new HashSet<>());
        when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility));
        // when
        facilityService.deleteFacility(1L);
        // then
        verify(facilityRepository).delete(facility);
    }
}

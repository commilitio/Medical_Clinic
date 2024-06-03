package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.mapper.VisitMapper;
import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.Visit;
import com.commilitio.medicalclinic.model.VisitCreateDto;
import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.repository.DoctorRepository;
import com.commilitio.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;

public class VisitServiceTest {
    private VisitService visitService;
    private VisitRepository visitRepository;
    private VisitMapper visitMapper;
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        visitRepository = Mockito.mock(VisitRepository.class);
        visitMapper = Mappers.getMapper(VisitMapper.class);
        doctorRepository = Mockito.mock(DoctorRepository.class);

        visitService = new VisitService(visitRepository, visitMapper, doctorRepository);
    }

    @Test
    void getVisits_VisitsDoNotExist_ReturnEmptyList() {
        // given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Visit> visitPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(visitRepository.findAll(pageable)).thenReturn(visitPage);
        when(visitMapper.toDtos(visitPage.toList())).thenReturn(Collections.emptyList());
        // when
        List<VisitDto> result = visitService.getVisits(pageable);
        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getVisits_CorrectData_ReturnVisits() {
        // given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);

        Visit visit1 = new Visit();
        visit1.setId(1L);
        visit1.setVisitStartTime(LocalDateTime.now().plusDays(1).withMinute(0));
        visit1.setVisitEndTime(LocalDateTime.now().plusDays(1).plusHours(1).withMinute(0));
        visit1.setDoctor(doctor1);

        Visit visit2 = new Visit();
        visit2.setId(2L);
        visit2.setVisitStartTime(LocalDateTime.now().plusDays(2));
        visit2.setVisitEndTime(LocalDateTime.now().plusDays(2).plusHours(1));
        visit2.setDoctor(doctor2);

        List<Visit> visits = List.of(visit1, visit2);
        Page<Visit> visitPage = new PageImpl<>(visits, pageable, visits.size());

        when(visitRepository.findAll(pageable)).thenReturn(visitPage);

        VisitDto visitDto1 = new VisitDto();
        visitDto1.setId(1L);
        visitDto1.setVisitStartTime(visit1.getVisitStartTime());
        visitDto1.setVisitEndTime(visit1.getVisitEndTime());
        visitDto1.setDoctorId(doctor1.getId());

        VisitDto visitDto2 = new VisitDto();
        visitDto2.setId(2L);
        visitDto2.setVisitStartTime(visit2.getVisitStartTime());
        visitDto2.setVisitEndTime(visit2.getVisitEndTime());
        visitDto2.setDoctorId(doctor2.getId());

        List<VisitDto> visitDtos = List.of(visitDto1, visitDto2);

        when(visitMapper.toDtos(visits)).thenReturn(visitDtos);
        // when
        List<VisitDto> result = visitService.getVisits(pageable);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(visitDto1.getId(), result.get(0).getId());
        Assertions.assertEquals(visitDto2.getId(), result.get(1).getId());
    }

    @Test
    void createVisit_VisitsOverlap_ThrowsIllegalArgumentException() {
        // given
        long doctorId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);

        VisitCreateDto visitCreateDto = new VisitCreateDto();
        visitCreateDto.setDoctorId(doctorId);
        visitCreateDto.setVisitStartTime(startTime);
        visitCreateDto.setVisitEndTime(endTime);
        List<Visit> overlappingVisits = List.of(new Visit());

        when(visitRepository.checkIfVisitsOverlap(doctorId, startTime, endTime)).thenReturn(overlappingVisits);
        // when
        Exception result = Assertions.assertThrows(IllegalArgumentException.class, () -> visitService.createVisit(visitCreateDto));
        // then
        Assertions.assertEquals("The doctor's visits is already occupied at the specified time.", result.getMessage());
    }

    @Test
    void createVisit_VisitHasExpired_ThrowsIllegalArgumentException() {
        // given
        VisitCreateDto visitCreateDto = new VisitCreateDto();
        visitCreateDto.setVisitStartTime(LocalDateTime.now().minusHours(2));
        visitCreateDto.setVisitEndTime(LocalDateTime.now().minusHours(1));
        // when
        Exception result = Assertions.assertThrows(IllegalArgumentException.class, () -> visitService.createVisit(visitCreateDto));
        // then
        Assertions.assertEquals("Chosen visit has already expired.", result.getMessage());
    }

    @Test
    void createVisit_InvalidVisitMinutes_ThrowsIllegalArgumentException() {
        // given
        VisitCreateDto visitCreateDto = new VisitCreateDto();
        visitCreateDto.setVisitStartTime(LocalDateTime.now().plusHours(1).withMinute(10));
        visitCreateDto.setVisitEndTime(LocalDateTime.now().plusHours(2).withMinute(20));
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        when(doctorRepository.findById(visitCreateDto.getDoctorId())).thenReturn(Optional.of(doctor));
        // when
        Exception result = Assertions.assertThrows(IllegalArgumentException.class, () -> visitService.createVisit(visitCreateDto));
        // then
        Assertions.assertEquals("The visit times must be in 15 minute intervals.", result.getMessage());
    }

    @Test
    void createVisit_CorrectData_ReturnCreatedVisit() {
        // given
        VisitCreateDto visitCreateDto = new VisitCreateDto();
        visitCreateDto.setDoctorId(1L);
        LocalDateTime startTime = LocalDateTime.now().plusHours(1).withMinute(0);
        LocalDateTime endTime = LocalDateTime.now().plusHours(2).withMinute(0);
        visitCreateDto.setVisitStartTime(startTime);
        visitCreateDto.setVisitEndTime(endTime);

        Doctor doctor = new Doctor();
        doctor.setId(1L);

        Visit createdVisit = new Visit();
        createdVisit.setId(1L);
        createdVisit.setVisitStartTime(startTime);
        createdVisit.setVisitEndTime(endTime);
        createdVisit.setDoctor(doctor);

        when(doctorRepository.findById(visitCreateDto.getDoctorId())).thenReturn(Optional.of(doctor));
        when(visitRepository.save(createdVisit)).thenReturn(createdVisit);
        // when
        VisitDto result = visitService.createVisit(visitCreateDto);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getDoctorId());
    }
}

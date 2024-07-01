package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.exception.DoctorException;
import com.commilitio.medicalclinic.exception.VisitException;
import com.commilitio.medicalclinic.mapper.VisitMapper;
import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.Visit;
import com.commilitio.medicalclinic.model.VisitCreateDto;
import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.repository.DoctorRepository;
import com.commilitio.medicalclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorRepository doctorRepository;

    public List<VisitDto> getVisits(Pageable pageable) {
        Page<Visit> visits = visitRepository.findAll(pageable);
        return visitMapper.toDtos(visits.toList());
    }

    public List<VisitDto> getVisitsBySpecialization(LocalDate visitDate, String specialization){
        List<Visit> visits = visitRepository.getVisitsBySpecialization(visitDate, specialization);
        return visits.stream()
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public VisitDto createVisit(VisitCreateDto visitCreateDto) {

        checkIfVisitsOverlap(visitCreateDto);
        validateVisitTime(visitCreateDto);

        Doctor doctor = doctorRepository.findById(visitCreateDto.getDoctorId())
                .orElseThrow(() -> new DoctorException("Doctor Not Found.", HttpStatus.NOT_FOUND));

        Visit createdVisit = new Visit();
        createdVisit.setVisitStartTime(visitCreateDto.getVisitStartTime());
        createdVisit.setVisitEndTime(visitCreateDto.getVisitEndTime());
        createdVisit.setDoctor(doctor);

        validateVisitMinutes(createdVisit);
        visitRepository.save(createdVisit);
        return visitMapper.toDto(createdVisit);
    }

    private void checkIfVisitsOverlap(VisitCreateDto visitCreateDto) {
        if (!visitRepository.checkIfVisitsOverlap(
                visitCreateDto.getDoctorId(),
                visitCreateDto.getVisitStartTime(),
                visitCreateDto.getVisitEndTime()).isEmpty()) {
            throw new VisitException("The doctor's visits is already occupied at the specified time.", HttpStatus.CONFLICT);
        }
    }

    protected void validateVisitTime(VisitCreateDto visitCreateDto) {
        if (visitCreateDto.getVisitStartTime() == null || visitCreateDto.getVisitEndTime() == null) {
            throw new VisitException("Visit start time and end time must not be null.", HttpStatus.BAD_REQUEST);
        }
        if (visitCreateDto.getVisitStartTime().isAfter(visitCreateDto.getVisitEndTime())) {
            throw new VisitException("Visit start time must be before end time.", HttpStatus.BAD_REQUEST);
        }
        if (visitCreateDto.getVisitStartTime().isBefore(LocalDateTime.now()) || visitCreateDto.getVisitEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Chosen visit has already expired.");
        }
    }

    protected void validateVisitMinutes(Visit visit) {
        if (visit.getVisitStartTime().getMinute() % 15 != 0 || visit.getVisitEndTime().getMinute() % 15 != 0) {
            throw new VisitException("The visit times must be in 15 minute intervals.", HttpStatus.BAD_REQUEST);
        }
    }
}

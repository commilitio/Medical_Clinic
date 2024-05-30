package com.commilitio.medicalclinic.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorRepository doctorRepository;

    public List<VisitDto> getVisits(Pageable pageable) {
        Page<Visit> patients = visitRepository.findAll(pageable);
        return visitMapper.toDtos(patients.toList());
    }

    @Transactional
    public VisitDto createVisit(VisitCreateDto visitCreateDto) {

        checkIfVisitsOverlap(visitCreateDto);
        validateVisitTime(visitCreateDto);

        Doctor doctor = doctorRepository.findById(visitCreateDto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor Not Found."));

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
            throw new IllegalArgumentException("The doctor's visits is already occupied at the specified time.");
        }
    }

    private void validateVisitTime(VisitCreateDto visitCreateDto) {
        if (visitCreateDto.getVisitStartTime().isBefore(LocalDateTime.now()) || visitCreateDto.getVisitEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Chosen visit has already expired.");
        }
    }

    private void validateVisitMinutes(Visit visit) {
        if (visit.getVisitStartTime().getMinute() % 15 != 0 || visit.getVisitEndTime().getMinute() % 15 != 0) {
            throw new IllegalArgumentException("The visit times must be in 15 minute intervals.");
        }
    }
}

package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.mapper.VisitMapper;
import com.commilitio.medicalclinic.model.Visit;
import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public List<VisitDto> getVisits(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Visit> patients = visitRepository.findAll(pageable);
        return visitMapper.toDtos(patients.toList());
    }

    @Transactional
    public VisitDto createVisit() {
        Visit createdVisit = new Visit();
        createdVisit.setVisitTime(LocalDateTime.now());
        visitRepository.save(createdVisit);
        return visitMapper.toDto(createdVisit);
    }
}

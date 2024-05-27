package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.mapper.VisitMapper;
import com.commilitio.medicalclinic.model.Visit;
import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    @Transactional
    public VisitDto createVisit() {
        Visit createdVisit = new Visit();
        createdVisit.setVisitTime(LocalDateTime.now());
        visitRepository.save(createdVisit);
        return visitMapper.toDto(createdVisit);
    }
}

package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.VisitCreateDto;
import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    @GetMapping
    public List<VisitDto> getVisits(Pageable pageable) {
        return visitService.getVisits(pageable);
    }

    @PostMapping
    public VisitDto createVisit(@RequestBody VisitCreateDto visitCreateDto) {
        return visitService.createVisit(visitCreateDto);
    }
}

package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public VisitDto createVisit() {
        return visitService.createVisit();
    }
}

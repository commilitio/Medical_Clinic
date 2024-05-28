package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    @GetMapping
    public List<VisitDto> getVisits(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return visitService.getVisits(page, size);
    }

    @PostMapping
    public VisitDto createVisit() {
        return visitService.createVisit();
    }
}

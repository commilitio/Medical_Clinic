package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.model.FacilityDto;
import com.commilitio.medicalclinic.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping("/{id}")
    public FacilityDto getFacility(@PathVariable Long id) {
        return facilityService.getFacility(id);
    }

    @GetMapping
    public List<FacilityDto> getFacilities() {
        return facilityService.getFacilities();
    }

    @PostMapping
    public FacilityDto addFacility(@RequestBody Facility facility) {
        return facilityService.addFacility(facility);
    }

    @DeleteMapping("/{id}")
    public void deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
    }
}
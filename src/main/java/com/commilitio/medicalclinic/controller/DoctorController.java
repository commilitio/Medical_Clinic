package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.DoctorDto;
import com.commilitio.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/{id}")
    public DoctorDto getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @GetMapping
    public List<DoctorDto> getDoctors(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return doctorService.getDoctors(page, size);
    }

    @PostMapping
    public DoctorDto addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @PatchMapping("/{doctorId}/facilities/{facilityId}")
    public DoctorDto assignDoctorToFacility(@PathVariable Long doctorId, @PathVariable Long facilityId) {
        return doctorService.assignDoctorToFacility(doctorId, facilityId);
    }
}

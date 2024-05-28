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

    @GetMapping("/{email}")
    public DoctorDto getDoctor(@PathVariable String email) {
        return doctorService.getDoctor(email);
    }

    @GetMapping
    public List<DoctorDto> getDoctors(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return doctorService.getDoctors(page, size);
    }

    @PostMapping
    public DoctorDto addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @DeleteMapping("/{email}")
    public void deleteDoctor(@PathVariable String email) {
        doctorService.deleteDoctor(email);
    }

    @PatchMapping("/{doctorId}/facilities/{facilityId}")
    public DoctorDto assignDoctorToFacility(@PathVariable Long doctorId, @PathVariable Long facilityId) {
        return doctorService.assignDoctorToFacility(doctorId, facilityId);
    }
}

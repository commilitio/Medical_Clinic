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
    public List<DoctorDto> getDoctors() {
        return doctorService.getDoctors();
    }

    @PostMapping
    public DoctorDto addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @DeleteMapping("/{email}")
    public void deleteDoctor(@PathVariable String email) {
        doctorService.deleteDoctor(email);
    }

    @PatchMapping("/{doctor_id}/facilities/{facility_id}")
    public DoctorDto assignDoctorToFacility(@PathVariable Long doctor_id, @PathVariable Long facility_id) {
        return doctorService.assignDoctorToFacility(doctor_id, facility_id);
    }
}

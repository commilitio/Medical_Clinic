package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import com.commilitio.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<PatientDto> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/{email}")
    public PatientDto getPatient(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @PostMapping
    public PatientDto addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @PutMapping("/{email}")
    public PatientDto updatePatient(@PathVariable String email, @RequestBody Patient patient) {
        return patientService.updatePatient(email, patient);
    }

    @PatchMapping("/{email}")
    public Patient updatePassword(@PathVariable String email, @RequestBody String password) {
        return patientService.updatePassword(email, password);
    }
}

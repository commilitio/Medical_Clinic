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
    public List<PatientDto> getPatients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return patientService.getPatients(page, size);
    }

    @GetMapping("/{id}")
    public PatientDto getPatient(@PathVariable Long id) {
        return patientService.getPatient(id);
    }

    @PostMapping
    public PatientDto addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @PutMapping("/{id}")
    public PatientDto updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @PatchMapping("/{id}/password")
    public PatientDto updatePassword(@PathVariable Long id, @RequestBody String password) {
        return patientService.updatePassword(id, password);
    }

    @PatchMapping("/{id}/visits/{visitId}")
    public PatientDto assignPatientToVisit(@PathVariable Long id, @PathVariable Long visitId) {
        return patientService.assignPatientToVisit(id, visitId);
    }
}

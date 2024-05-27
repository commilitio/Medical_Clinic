package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.mapper.PatientMapper;
import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import com.commilitio.medicalclinic.model.Visit;
import com.commilitio.medicalclinic.repository.PatientRepository;
import com.commilitio.medicalclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final VisitRepository visitRepository;

    public List<PatientDto> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patientMapper.toDtos(patients);
    }

    public PatientDto getPatient(String email) {
        Patient patient = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        return patientMapper.toDto(patient);
    }

    @Transactional
    public PatientDto addPatient(Patient patient) {
        boolean patientExists = patientRepository
                .findPatientByEmail(patient.getEmail())
                .isPresent();
        if (patientExists) {
            throw new IllegalArgumentException("Patient already exists.");
        }
        Patient addedPatient = patientRepository.save(patient);
        return patientMapper.toDto(addedPatient);
    }

    public void deletePatient(String email) {
        Patient patient = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        patientRepository.delete(patient);
    }

    @Transactional
    public PatientDto updatePatient(String email, Patient patient) {
        Patient patientToUpdate = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        patientToUpdate.update(patient);
        Patient updatedPatient = patientRepository.save(patientToUpdate);
        return patientMapper.toDto(updatedPatient);
    }

    @Transactional
    public PatientDto updatePassword(String email, String password) {
        Patient patientToUpdate = patientRepository.findPatientByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        patientToUpdate.setPassword(password);
        Patient updatedPatient = patientRepository.save(patientToUpdate);
        return patientMapper.toDto(updatedPatient);
    }

    @Transactional
    public PatientDto assignPatientToVisit(Long id, Long visitId) {
        Patient patientToAssign = patientRepository.findPatientById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        Visit visit = visitRepository.findVisitById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit Not Found."));
        visit.setPatient(patientToAssign);

        visitRepository.save(visit);
        return patientMapper.toDto(patientToAssign);
    }
}

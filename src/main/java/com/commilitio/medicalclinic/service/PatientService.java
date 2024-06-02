package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.mapper.PatientMapper;
import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import com.commilitio.medicalclinic.model.User;
import com.commilitio.medicalclinic.model.Visit;
import com.commilitio.medicalclinic.repository.PatientRepository;
import com.commilitio.medicalclinic.repository.UserRepository;
import com.commilitio.medicalclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    public List<PatientDto> getPatients(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Patient> patients = patientRepository.findAll(pageable);
        if (patients.isEmpty()) {
            throw new IllegalArgumentException("No patients found.");
        }
        return patientMapper.toDtos(patients.toList());
    }

    public PatientDto getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        return patientMapper.toDto(patient);
    }

    @Transactional
    public PatientDto addPatient(Patient patient) {
        boolean patientExists = userRepository
                .findPatientByEmail(patient.getUser().getEmail())
                .isPresent();
        if (patientExists) {
            throw new IllegalArgumentException("Patient already exists.");
        }
        User user = new User();
        user.setFirstName(patient.getUser().getFirstName());
        user.setLastName(patient.getUser().getLastName());
        user.setEmail(patient.getUser().getEmail());
        user.setPassword(patient.getUser().getPassword());

        patient.setUser(user);
        Patient addedPatient = patientRepository.save(patient);
        return patientMapper.toDto(addedPatient);
    }

    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        patientRepository.delete(patient);
    }

    @Transactional
    public PatientDto updatePatient(Long id, Patient patient) {
        Patient patientToUpdate = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        patientToUpdate.update(patient);
        Patient updatedPatient = patientRepository.save(patientToUpdate);
        return patientMapper.toDto(updatedPatient);
    }

    @Transactional
    public PatientDto updatePassword(Long id, String password) {
        Patient patientToUpdate = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        patientToUpdate.getUser().setPassword(password);
        Patient updatedPatient = patientRepository.save(patientToUpdate);
        return patientMapper.toDto(updatedPatient);
    }

    @Transactional
    public PatientDto assignPatientToVisit(Long id, Long visitId) {
        Patient patientToAssign = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
        Visit visit = visitRepository.findVisitById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit Not Found."));
        if (visit.getVisitStartTime().isBefore(LocalDateTime.now()) || visit.getVisitEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Chosen visit has already expired.");
        }
        visit.setPatient(patientToAssign);

        visitRepository.save(visit);
        return patientMapper.toDto(patientToAssign);
    }
}

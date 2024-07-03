package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.exception.PatientException;
import com.commilitio.medicalclinic.exception.VisitException;
import com.commilitio.medicalclinic.mapper.PatientMapper;
import com.commilitio.medicalclinic.mapper.VisitMapper;
import com.commilitio.medicalclinic.model.*;
import com.commilitio.medicalclinic.repository.DoctorRepository;
import com.commilitio.medicalclinic.repository.PatientRepository;
import com.commilitio.medicalclinic.repository.UserRepository;
import com.commilitio.medicalclinic.repository.VisitRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Builder
@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final VisitMapper visitMapper;

    public List<PatientDto> getPatients(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Patient> patients = patientRepository.findAll(pageable);
        if (patients.isEmpty()) {
            throw new PatientException("No patients found.", HttpStatus.NOT_FOUND);
        }
        return patientMapper.toDtos(patients.toList());
    }

    public PatientDto getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient Not Found.", HttpStatus.NOT_FOUND));
        return patientMapper.toDto(patient);
    }

    public List<VisitDto> getPatientVisits(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient Not Found.", HttpStatus.NOT_FOUND));
        List<Visit> visits = new ArrayList<>(patient.getVisits());
        return visitMapper.toDtos(visits);
    }

    @Transactional
    public PatientDto addPatient(Patient patient) {
        boolean patientExists = userRepository
                .findPatientByEmail(patient.getUser().getEmail())
                .isPresent();
        if (patientExists) {
            throw new PatientException("Patient already exists.", HttpStatus.CONFLICT);
        }
        User user = User.builder()
                .firstName(patient.getUser().getFirstName())
                .lastName(patient.getUser().getLastName())
                .email(patient.getUser().getEmail())
                .password(patient.getUser().getPassword())
                .build();

        patient.setUser(user);
        Patient addedPatient = patientRepository.save(patient);
        log.info("Created patient with data: {}", patient);
        return patientMapper.toDto(addedPatient);
    }

    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient Not Found.", HttpStatus.NOT_FOUND));
        patientRepository.delete(patient);
        log.info("Deleted patient with id: {}", id);
    }

    @Transactional
    public PatientDto updatePatient(Long id, Patient patient) {
        Patient patientToUpdate = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient Not Found.", HttpStatus.NOT_FOUND));
        patientToUpdate.update(patient);
        Patient updatedPatient = patientRepository.save(patientToUpdate);
        log.info("Updated patient with id {} with new data: {}", id, patient);
        return patientMapper.toDto(updatedPatient);
    }

    @Transactional
    public PatientDto updatePassword(Long id, String password) {
        Patient patientToUpdate = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient Not Found.", HttpStatus.NOT_FOUND));
        patientToUpdate.getUser().setPassword(password);
        Patient updatedPatient = patientRepository.save(patientToUpdate);
        log.info("Patient with id {} has updated password", id);
        return patientMapper.toDto(updatedPatient);
    }

    @Transactional
    public PatientDto assignPatientToVisit(Long id, Long visitId) {
        Patient patientToAssign = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient Not Found.", HttpStatus.NOT_FOUND));
        Visit visit = visitRepository.findVisitById(visitId)
                .orElseThrow(() -> new VisitException("Visit Not Found.", HttpStatus.NOT_FOUND));
        if (visit.getVisitStartTime().isBefore(LocalDateTime.now()) || visit.getVisitEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Chosen visit has already expired.");
        }
        visit.setPatient(patientToAssign);
        patientToAssign.getDoctors().add(visit.getDoctor());
        patientToAssign.getVisits().add(visit);
        patientRepository.save(patientToAssign);

        Doctor doctor = visit.getDoctor();
        doctor.getPatients().add(patientToAssign);
        doctorRepository.save(doctor);

        visitRepository.save(visit);
        log.info("Assigned patient with data: {}, to the visit with data: {}", patientToAssign, visit);
        return patientMapper.toDto(patientToAssign);
    }
}

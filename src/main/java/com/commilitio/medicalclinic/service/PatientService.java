package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import com.commilitio.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientDto> getPatients(){
        return patientRepository.getPatients().stream()
                .map(this::convertToDto).
                collect(Collectors.toList());
    }

    public PatientDto getPatient(String email){
        return patientRepository.getPatient(email)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
    }

    public Patient addPatient(Patient patient) {
        return patientRepository.addPatient(patient)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
    }

    public void deletePatient(String email) {
        patientRepository.deletePatient(email);
    }

    public PatientDto updatePatient(String email, Patient patient) {
        PatientDto patientToUpdate = patientRepository.getPatient(email)            // getPatient powyzej
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found."));
        patientToUpdate.setEmail(patient.getEmail());
        patientToUpdate.setIdCardNo(patient.getIdCardNo());         // removed setPassword
        patientToUpdate.setFirstName(patient.getFirstName());
        patientToUpdate.setLastName(patient.getLastName());
        patientToUpdate.setPhoneNumber(patient.getPhoneNumber());
        patientToUpdate.setBirthdate(patient.getBirthdate());
        return patientToUpdate;
    }

    public Patient updatePassword(String email, String password) {
        return patientRepository.updatePassword(email, password);
    }

    private PatientDto convertToDto(Patient patient) {
        PatientDto patientDto = new PatientDto();
        patientDto.setEmail(patient.getEmail());
        patientDto.setIdCardNo(patient.getIdCardNo());
        patientDto.setFirstName(patient.getFirstName());
        patientDto.setLastName(patient.getLastName());
        patientDto.setPhoneNumber(patient.getPhoneNumber());
        patientDto.setBirthdate(patient.getBirthdate());
        return patientDto;
    }
}

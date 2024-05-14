package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getPatients(){
        return patientRepository.getPatients();
    }

    public Patient getPatient(String email){
        return patientRepository.getPatient(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
    }

    public Patient addPatient(Patient patient) {
        return patientRepository.addPatient(patient)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found."));
    }

    public void deletePatient(String email) {
        patientRepository.deletePatient(email);
    }

    public Patient updatePatient(String email, Patient patient) {
        Patient patientToUpdate = patientRepository.getPatient(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found."));
        patientToUpdate.setEmail(patient.getEmail());
        patientToUpdate.setPassword(patient.getPassword());
        patientToUpdate.setIdCardNo(patient.getIdCardNo());
        patientToUpdate.setFirstName(patient.getFirstName());
        patientToUpdate.setLastName(patient.getLastName());
        patientToUpdate.setPhoneNumber(patient.getPhoneNumber());
        patientToUpdate.setBirthdate(patient.getBirthdate());
        return patientToUpdate;
    }

    public Patient updatePassword(String email, String password) {
        return patientRepository.updatePassword(email, password);
    }
}

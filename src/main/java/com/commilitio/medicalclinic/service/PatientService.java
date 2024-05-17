package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.mapper.PatientMapper;
import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import com.commilitio.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDto> getPatients(){
        return patientMapper.mapListToDto(patientRepository.getPatients());
    }

    public PatientDto getPatient(String email){
        return patientMapper.patientToPatientDto(patientRepository.getPatient(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient Not Found.")));
    }

    public PatientDto addPatient(Patient patient) {
        return patientMapper.patientToPatientDto(patientRepository.addPatient(patient)
                .orElseThrow(() -> new IllegalArgumentException("Operation failed.")));
    }

    public void deletePatient(String email) {
        patientRepository.deletePatient(email);
    }

    public PatientDto updatePatient(String email, Patient patient) {
        return patientMapper.patientToPatientDto(patientRepository.updatePatient(email, patient));
    }

    public Patient updatePassword(String email, String password) {
        return patientRepository.updatePassword(email, password);
    }
}

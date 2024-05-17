package com.commilitio.medicalclinic.repository;

import com.commilitio.medicalclinic.model.Patient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
@Repository
public class PatientRepository {

    private final List<Patient> patients;

    public Optional<Patient> getPatient(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    public Optional<Patient> addPatient(Patient patient) {
        boolean patientExists = patients.stream()
                .anyMatch(existingPatient -> existingPatient.getEmail().equals(patient.getEmail()));
        if (patientExists) {
            return Optional.empty();
        }
        patients.add(patient);
        return Optional.of(patient);
    }

    public boolean deletePatient(String email) {
        Optional<Patient> patientToDelete = getPatient(email);
        if (patientToDelete.isPresent()) {
            patients.remove(patientToDelete.get());
            return true;
        }
        return false;
    }

    public Optional<Patient> updatePatient(String email, Patient patient) {
        Optional<Patient> patientToUpdate = getPatient(email);
        if (patientToUpdate.isPresent()) {
            patientToUpdate.get().setEmail(patient.getEmail());
            patientToUpdate.get().setPassword(patient.getPassword());
            patientToUpdate.get().setIdCardNo(patient.getIdCardNo());
            patientToUpdate.get().setFirstName(patient.getFirstName());
            patientToUpdate.get().setLastName(patient.getLastName());
            patientToUpdate.get().setPhoneNumber(patient.getPhoneNumber());
            patientToUpdate.get().setBirthdate(patient.getBirthdate());
            return patientToUpdate;
        }
        return Optional.empty();
    }

    public Optional<Patient> updatePassword(String email, String password) {
        Optional<Patient> patientToUpdate = getPatient(email);
        if (patientToUpdate.isPresent()) {
            patientToUpdate.get().setPassword(password);
            return patientToUpdate;
        }
        return Optional.empty();
    }
}

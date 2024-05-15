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
            throw new IllegalArgumentException("Patient already exists.");
        } else {
            patients.add(patient);
            System.out.println("Patient successfully added.");
            return Optional.of(patient);
        }
    }

    public void deletePatient(String email) {
        Optional<Patient> patientToDelete = patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();

        if (patientToDelete.isEmpty()) {
            throw new IllegalArgumentException("Patient not found.");
        } else {
            patients.remove(patientToDelete.get());
            System.out.println("Patient successfully deleted.");
        }
    }

    public Patient updatePassword(String email, String password) {
        Patient updatedPatient = patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        updatedPatient.setPassword(password);
        return updatedPatient;
    }
}

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

    private final List<Patient> patientList;

    public Optional<Patient> getPatient(String email) {
        return Optional.of((Patient) patientList.stream()
                .filter(patient -> patient.getEmail().equals(email)));
    }

    public Optional<Patient> addPatient(Patient patient) {
        boolean patientExists = patientList.stream()
                .anyMatch(existingPatient -> existingPatient.getIdCardNo().equals(patient.getIdCardNo()));

        if (patientExists) {
            System.out.println("Patient already exists.");
            return Optional.empty();
        } else {
            patientList.add(patient);
            System.out.println("Patient successfully added.");
            return Optional.of(patient);
        }
    }

    public void deletePatient(String email) {
        Optional<Patient> patientToDelete = patientList.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();

        if (patientToDelete.isEmpty()) {
            System.out.println("Patient not found.");
        } else {
            patientList.remove(patientToDelete.get());
            System.out.println("Patient successfully deleted.");
        }
    }

    public Optional<Patient> updatePatient(String email, Patient patient) {
        Optional<Patient> patientToUpdate = patientList.stream()
                .filter(element -> element.getEmail().equals(email))
                .findFirst();

        if (patientToUpdate.isEmpty()) {
            System.out.println("Patient not found.");
            return Optional.empty();
        } else {
            patientToUpdate.get().setEmail(patient.getEmail());
            patientToUpdate.get().setPassword(patient.getPassword());
            patientToUpdate.get().setIdCardNo(patient.getIdCardNo());
            patientToUpdate.get().setFirstName(patient.getFirstName());
            patientToUpdate.get().setLastName(patient.getLastName());
            patientToUpdate.get().setPhoneNumber(patient.getPhoneNumber());
            patientToUpdate.get().setBirthdate(patient.getBirthdate());
        }
        return patientToUpdate;
    }
}























package com.commilitio.medicalclinic.repository;

import com.commilitio.medicalclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

  //  @Query("SELECT p FROM Patient p WHERE p.user.email = :email")
  //  Optional<Patient> findPatientByEmail(String email);

}

package com.commilitio.medicalclinic.repository;

import com.commilitio.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findDoctorByEmail(String email);
}

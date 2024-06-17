package com.commilitio.medicalclinic.repository;

import com.commilitio.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d WHERE d.user.email = :email")
    Optional<Doctor> findDoctorByEmail(@Param("email") String email);
}

package com.commilitio.medicalclinic.repository;

import com.commilitio.medicalclinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    Optional<Visit> findVisitById(Long id);

    @Query("SELECT v FROM Visit v " +
            "WHERE v.doctor.id = :doctorId " +
            "AND v.visitStartTime <= :visitEndTime " +
            "AND v.visitEndTime >= :visitStartTime ")
    List<Visit> checkIfVisitsOverlap(Long doctorId, LocalDateTime visitStartTime, LocalDateTime visitEndTime);

    @Query("SELECT v FROM Visit v " +
            "WHERE v.doctor.specialization = :specialization " +
            "AND v.patient IS NULL " +
            "AND DATE(v.visitStartTime) = :visitDate")
    List<Visit> getVisitsBySpecialization(LocalDate visitDate, String specialization);
}

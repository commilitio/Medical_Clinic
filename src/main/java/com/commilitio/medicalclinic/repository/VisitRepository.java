package com.commilitio.medicalclinic.repository;

import com.commilitio.medicalclinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    Optional<Visit> findVisitById(Long id);
}

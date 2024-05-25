package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "VISIT")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "VISIT_TIME")
    private LocalDateTime visitTime;
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;
}

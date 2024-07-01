package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "VISIT")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "VISIT_START_TIME")
    private LocalDateTime visitStartTime;
    @Column(nullable = false, name = "VISIT_END_TIME")
    private LocalDateTime visitEndTime;
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    @EqualsAndHashCode.Exclude          // StackOverflow err
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;
}

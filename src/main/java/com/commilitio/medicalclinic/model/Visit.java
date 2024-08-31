package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
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

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", visitStartTime=" + visitStartTime +
                ", visitEndTime=" + visitEndTime +
                ", patientId=" + (patient != null ? patient.getId() : null) +
                ", doctorId=" + (doctor != null ? doctor.getId() : null) +
                '}';
    }
}

package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "PATIENT")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, name = "ID_CARD_NO")
    private String idCardNo;
    @Column(nullable = false, name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(nullable = false, name = "BIRTHDATE")
    private LocalDate birthdate;
    @OneToMany(mappedBy = "patient")
    private Set<Visit> visits = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToMany(mappedBy = "patients", cascade = {CascadeType.ALL})
    private Set<Doctor> doctors = new HashSet<>();

    public void update(Patient updatedPatient) {
        this.user = updatedPatient.getUser();
        this.idCardNo = updatedPatient.getIdCardNo();
        this.phoneNumber = updatedPatient.getPhoneNumber();
        this.birthdate = updatedPatient.getBirthdate();
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", idCardNo='" + idCardNo + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthdate=" + birthdate +
                ", visits=" + visits.stream()
                .map(visit -> visit.getId())
                .collect(Collectors.toSet()) +
                ", user=" + user +
                ", doctors=" + doctors.stream()
                .map(doctor -> String.format("Doctor{id=%d, firstName='%s', lastName='%s', specialization='%s'}",
                        doctor.getId(),
                        doctor.getUser().getFirstName(),
                        doctor.getUser().getLastName(),
                        doctor.getSpecialization()))
                .collect(Collectors.toSet()) +
                '}';
    }
}

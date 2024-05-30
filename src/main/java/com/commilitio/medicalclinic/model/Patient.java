package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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

    public void update(Patient updatedPatient) {
        this.user = updatedPatient.getUser();
        this.idCardNo = updatedPatient.getIdCardNo();
        this.phoneNumber = updatedPatient.getPhoneNumber();
        this.birthdate = updatedPatient.getBirthdate();
    }
}

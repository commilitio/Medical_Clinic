package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, name = "EMAIL")
    private String email;
    @Column(nullable = false, name = "PASSWORD")
    private String password;
    @Column(nullable = false, unique = true, name = "ID_CARD_NO")
    private String idCardNo;
    @Column(nullable = false, name = "FIRST_NAME")
    private String firstName;
    @Column(nullable = false, name = "LAST_NAME")
    private String lastName;
    @Column(nullable = false, name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(nullable = false, name = "BIRTHDATE")
    private LocalDate birthdate;
    @OneToMany(mappedBy = "patient")
    private Set<Visit> visits = new HashSet<>();

    public void update(Patient updatedPatient) {
        this.email = updatedPatient.getEmail();
        this.password = updatedPatient.getPassword();
        this.idCardNo = updatedPatient.getIdCardNo();
        this.firstName = updatedPatient.getFirstName();
        this.lastName = updatedPatient.getLastName();
        this.phoneNumber = updatedPatient.getPhoneNumber();
        this.birthdate = updatedPatient.getBirthdate();
    }
}

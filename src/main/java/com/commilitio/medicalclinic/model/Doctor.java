package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "DOCTOR")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, name = "EMAIL")
    private String email;
    @Column(nullable = false, name = "PASSWORD")
    private String password;
    @Column(nullable = false, name = "FIRST_NAME")
    private String firstName;
    @Column(nullable = false, name = "LAST_NAME")
    private String lastName;
    @Column(nullable = false, name = "SPECIALIZATION")
    private String specialization;

    @ManyToMany
    @JoinTable(
            name = "DOCTOR_FACILITY",
            joinColumns = {@JoinColumn(name = "DOCTOR_ID")},
            inverseJoinColumns = {@JoinColumn(name = "FACILITY_ID")}
    )
    private Set<Facility> facilities = new HashSet<>();

}

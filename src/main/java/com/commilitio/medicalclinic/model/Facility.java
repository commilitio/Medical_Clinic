package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "FACILITY")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "NAME", unique = true)
    private String name;
    @Column(nullable = false, name = "CITY")
    private String city;
    @Column(nullable = false, name = "ZIP_CODE")
    private String zipCode;
    @Column(nullable = false, name = "STREET_NAME")
    private String streetName;
    @Column(nullable = false, name = "FIRST_NUMBER")
    private String streetNumber;

}

package com.commilitio.medicalclinic.model;

import lombok.Data;

@Data
public class FacilitySimpleDto {
    private Long id;
    private String name;
    private String city;
    private String zipCode;
    private String streetName;
    private String streetNumber;
}

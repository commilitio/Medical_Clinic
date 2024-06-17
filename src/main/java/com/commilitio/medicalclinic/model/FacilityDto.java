package com.commilitio.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDto {

    private Long id;
    private String name;
    private String city;
    private String zipCode;
    private String streetName;
    private String streetNumber;
    private Set<Long> doctors;
}

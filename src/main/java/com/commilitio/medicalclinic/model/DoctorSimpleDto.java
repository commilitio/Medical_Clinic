package com.commilitio.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorSimpleDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
}

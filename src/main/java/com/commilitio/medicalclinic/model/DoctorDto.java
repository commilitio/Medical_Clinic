package com.commilitio.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class DoctorDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String specialization;
    private Set<Long> facilities;
}

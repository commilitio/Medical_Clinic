package com.commilitio.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class PatientDto {

    private Long id;
    private String email;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthdate;
    private Set<String> doctors;
}

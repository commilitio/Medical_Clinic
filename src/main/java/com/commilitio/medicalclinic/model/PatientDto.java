package com.commilitio.medicalclinic.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientDto {

    private Long id;
    private String email;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthdate;
}

package com.commilitio.medicalclinic.model;

import lombok.*;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class DoctorDto {

    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String specialization;
    private final Set<Long> facilities;
    private final Set<PatientSimpleDto> patients;
}

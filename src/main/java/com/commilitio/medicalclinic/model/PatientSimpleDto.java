package com.commilitio.medicalclinic.model;

import lombok.*;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PatientSimpleDto {

    private final Long id;
    private final String firstName;
    private final String lastName;
}
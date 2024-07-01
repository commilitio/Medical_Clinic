package com.commilitio.medicalclinic.model;

import lombok.*;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class DoctorSimpleDto {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String specialization;
}

package com.commilitio.medicalclinic.model;

import lombok.*;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class FacilitySimpleDto {
    private final Long id;
    private final String name;
    private final String city;
    private final String zipCode;
    private final String streetName;
    private final String streetNumber;
}

package com.commilitio.medicalclinic.model;

import lombok.*;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class FacilityDto {

    private final Long id;
    private final String name;
    private final String city;
    private final String zipCode;
    private final String streetName;
    private final String streetNumber;
    private final Set<Long> doctors;
}

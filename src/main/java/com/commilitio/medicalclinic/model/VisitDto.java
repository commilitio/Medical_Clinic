package com.commilitio.medicalclinic.model;

import lombok.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class VisitDto {

    private final Long id;
    private final LocalDateTime visitStartTime;
    private final LocalDateTime visitEndTime;
    private final Long patientId;
    private final Long doctorId;
}

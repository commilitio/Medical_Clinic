package com.commilitio.medicalclinic.model;

import lombok.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class VisitCreateDto {

    private final Long doctorId;
    private final LocalDateTime visitStartTime;
    private final LocalDateTime visitEndTime;
}

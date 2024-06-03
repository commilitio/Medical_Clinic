package com.commilitio.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitDto {

    private Long id;
    private LocalDateTime visitStartTime;
    private LocalDateTime visitEndTime;
    private Long patientId;
    private Long doctorId;
}

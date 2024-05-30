package com.commilitio.medicalclinic.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitDto {

    private Long id;
    private LocalDateTime visitStartTime;
    private LocalDateTime visitEndTime;
    private Long patientId;
    private Long doctorId;
}

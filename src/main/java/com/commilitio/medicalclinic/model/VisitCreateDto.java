package com.commilitio.medicalclinic.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitCreateDto {

    private Long doctorId;
    private LocalDateTime visitStartTime;
    private LocalDateTime visitEndTime;
}

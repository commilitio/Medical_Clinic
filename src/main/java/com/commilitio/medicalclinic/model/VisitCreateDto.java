package com.commilitio.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitCreateDto {

    private Long doctorId;
    private LocalDateTime visitStartTime;
    private LocalDateTime visitEndTime;
}

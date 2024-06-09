package com.commilitio.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MessageDto {
    private String message;
    private LocalDate date;
    private HttpStatus httpStatus;
}

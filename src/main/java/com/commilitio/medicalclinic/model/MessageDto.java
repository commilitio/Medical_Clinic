package com.commilitio.medicalclinic.model;

import lombok.*;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class MessageDto {
    private final String message;
    private final LocalDate date;
    private final HttpStatus httpStatus;
}

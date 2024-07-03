package com.commilitio.medicalclinic.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class VisitException extends MedicalClinicException {

    public VisitException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}

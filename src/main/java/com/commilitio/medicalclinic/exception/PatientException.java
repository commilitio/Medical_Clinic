package com.commilitio.medicalclinic.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class PatientException extends MedicalClinicException {

    public PatientException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}

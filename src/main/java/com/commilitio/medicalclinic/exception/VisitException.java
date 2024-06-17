package com.commilitio.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class VisitException extends MedicalClinicException {

    public VisitException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}

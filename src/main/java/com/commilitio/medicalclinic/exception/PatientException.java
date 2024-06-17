package com.commilitio.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class PatientException extends MedicalClinicException {

    public PatientException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}

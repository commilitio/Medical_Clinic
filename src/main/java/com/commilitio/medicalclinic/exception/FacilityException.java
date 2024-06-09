package com.commilitio.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class FacilityException extends MedicalClinicException{

    public FacilityException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}

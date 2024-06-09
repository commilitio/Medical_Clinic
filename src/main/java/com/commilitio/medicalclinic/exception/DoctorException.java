package com.commilitio.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class DoctorException extends MedicalClinicException{

    public DoctorException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}

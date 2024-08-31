package com.commilitio.medicalclinic.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class FacilityException extends MedicalClinicException {

    public FacilityException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}

package com.commilitio.medicalclinic.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class MedicalClinicException extends RuntimeException {

    private final HttpStatus httpStatus;

    public MedicalClinicException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}

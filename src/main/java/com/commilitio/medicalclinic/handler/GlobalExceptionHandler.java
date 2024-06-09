package com.commilitio.medicalclinic.handler;

import com.commilitio.medicalclinic.exception.DoctorException;
import com.commilitio.medicalclinic.exception.FacilityException;
import com.commilitio.medicalclinic.exception.PatientException;
import com.commilitio.medicalclinic.exception.VisitException;
import com.commilitio.medicalclinic.model.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(DoctorException.class)
    public ResponseEntity<MessageDto> handleDoctorException(DoctorException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new MessageDto(e.getMessage(), LocalDate.now(), e.getHttpStatus()));
    }

    @ExceptionHandler(FacilityException.class)
    public ResponseEntity<MessageDto> handleFacilityException(FacilityException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new MessageDto(e.getMessage(), LocalDate.now(), e.getHttpStatus()));
    }

    @ExceptionHandler(PatientException.class)
    public ResponseEntity<MessageDto> handlePatientException(PatientException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new MessageDto(e.getMessage(), LocalDate.now(), e.getHttpStatus()));
    }

    @ExceptionHandler(VisitException.class)
    public ResponseEntity<MessageDto> handleVisitException(VisitException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new MessageDto(e.getMessage(), LocalDate.now(), e.getHttpStatus()));
    }
}

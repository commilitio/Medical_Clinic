package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDto patientToPatientDto(Patient patient);
    List<PatientDto> mapListToDto(List<Patient> patients);
}

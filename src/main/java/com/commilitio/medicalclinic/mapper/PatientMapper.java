package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    PatientDto toDto(Patient patient);

    List<PatientDto> toDtos(List<Patient> patients);
}

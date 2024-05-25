package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.DoctorDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto toDto(Doctor doctor);
    List<DoctorDto> toDtos(List<Doctor> doctors);
}

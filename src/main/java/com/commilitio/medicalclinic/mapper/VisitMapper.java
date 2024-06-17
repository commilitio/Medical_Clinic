package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Visit;
import com.commilitio.medicalclinic.model.VisitDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mapping(source = "patient.id", target = "patientId")       // czyt. Visit (Patient patient.getId() ==> VisitDto (Long patientId)
    @Mapping(source = "doctor.id", target = "doctorId")
    VisitDto toDto(Visit visit);

    List<VisitDto> toDtos(List<Visit> visits);
}
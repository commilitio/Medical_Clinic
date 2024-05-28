package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Visit;
import com.commilitio.medicalclinic.model.VisitDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    VisitDto toDto(Visit visit);
    List<VisitDto> toDtos(List<Visit> visits);
}
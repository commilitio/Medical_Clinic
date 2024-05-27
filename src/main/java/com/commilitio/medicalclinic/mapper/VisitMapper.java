package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Visit;
import com.commilitio.medicalclinic.model.VisitDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    VisitDto toDto(Visit visit);
}
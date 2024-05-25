package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.model.FacilityDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FacilityMapper {
    FacilityDto toDto(Facility facility);
    List<FacilityDto> toDtos(List<Facility> facilities);
}
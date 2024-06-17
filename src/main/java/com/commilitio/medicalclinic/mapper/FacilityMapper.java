package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.model.FacilityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FacilityMapper {

    @Mapping(target = "doctors", qualifiedByName = "mapToDoctor")
    FacilityDto toDto(Facility facility);

    List<FacilityDto> toDtos(List<Facility> facilities);

    @Named("mapToDoctor")
    default Set<Long> mapDoctors(Set<Doctor> doctors) {
        if (doctors == null) {
            return new HashSet<>();
        }
        return doctors.stream()
                .map(Doctor::getId)
                .collect(Collectors.toSet());
    }
}
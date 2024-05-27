package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.DoctorDto;
import com.commilitio.medicalclinic.model.Facility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(target = "facilities", qualifiedByName = "mapToFacility")
    DoctorDto toDto(Doctor doctor);

    @Mapping(target = "facilities", qualifiedByName = "mapToFacility")
    List<DoctorDto> toDtos(List<Doctor> doctors);

    @Named("mapToFacility")
    default Set<Long> mapFacilities(Set<Facility> facilities) {
        if (facilities == null) {
            return new HashSet<>();
        }
        return facilities.stream()
                .map(Facility::getId)
                .collect(Collectors.toSet());
    }
}

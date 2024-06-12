package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "facilities", qualifiedByName = "mapToFacility")
    @Mapping(target = "patients", qualifiedByName = "mapPatients")
    DoctorDto toDto(Doctor doctor);

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

    @Named("mapPatients")
    default Set<PatientSimpleDto> mapPatients(Set<Patient> patients) {
        if (patients == null) {
            return new HashSet<>();
        }
        return patients.stream()
                .map(patient -> new PatientSimpleDto(
                        patient.getId(),
                        patient.getUser().getFirstName(),
                        patient.getUser().getLastName()))
                .collect(Collectors.toSet());
    }
}

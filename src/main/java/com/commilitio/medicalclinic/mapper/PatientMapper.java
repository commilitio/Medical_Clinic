package com.commilitio.medicalclinic.mapper;

import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.DoctorSimpleDto;
import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "doctors", qualifiedByName = "mapToDoctor")
    PatientDto toDto(Patient patient);

    List<PatientDto> toDtos(List<Patient> patients);

    @Named("mapToDoctor")
    default Set<DoctorSimpleDto> mapDoctors(Set<Doctor> doctors) {
        if (doctors == null) {
            return new HashSet<>();
        }
        return doctors.stream()
                .map(doctor -> new DoctorSimpleDto(
                        doctor.getId(),
                        doctor.getUser().getFirstName(),
                        doctor.getUser().getLastName(),
                        doctor.getSpecialization()))
                .collect(Collectors.toSet());
    }
}

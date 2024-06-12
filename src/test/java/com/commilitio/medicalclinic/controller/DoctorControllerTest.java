package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.*;
import com.commilitio.medicalclinic.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @MockBean
    DoctorService doctorService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void getDoctor_CorrectData_ReturnDoctor() throws Exception {
        // given
        DoctorDto doctorDto = new DoctorDto(1L, "bob@wp.pl", "Bob", "Pop", "Cardiologist", new HashSet<>(), new HashSet<>());
        when(doctorService.getDoctor(1L)).thenReturn(doctorDto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(doctorDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(doctorDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(doctorDto.getLastName()))
                .andExpect(jsonPath("$.specialization").value(doctorDto.getSpecialization()));
    }

    @Test
    void getDoctors_CorrectData_ReturnDoctors() throws Exception {
        // given
        DoctorDto doctorDto1 = new DoctorDto(1L, "bob@wp.pl", "Bob", "Pop", "Cardiologist", new HashSet<>(), new HashSet<>());
        DoctorDto doctorDto2 = new DoctorDto(2L, "jan@wp.pl", "Jan", "Pop", "Cardiologist", new HashSet<>(), new HashSet<>());
        List<DoctorDto> doctors = List.of(doctorDto1, doctorDto2);
        when(doctorService.getDoctors(0, 10)).thenReturn(doctors);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(doctorDto1.getEmail()))
                .andExpect(jsonPath("$[1].email").value(doctorDto2.getEmail()));
    }

    @Test
    void addDoctor_CorrectData_ReturnDoctor() throws Exception {
        // given
        User user = new User(1L, "Bob", "Pop", "bob@wp.pl", "pass123");
        Patient patient = new Patient(1L, "23342", "500600700", LocalDate.of(1980, 1, 1), new HashSet<>(), user, new HashSet<>());
        Set<Patient> patients = new HashSet<>();
        patients.add(patient);

        Doctor doctor = new Doctor(1L, "Cardiologist", new HashSet<>(), user, new HashSet<>(), patients);

        Set<PatientSimpleDto> patientSimpleDtos = patients.stream()
                .map(p -> new PatientSimpleDto(
                        p.getId(),
                        p.getUser().getFirstName(),
                        p.getUser().getLastName()))
                .collect(Collectors.toSet());

        DoctorDto doctorDto = new DoctorDto(
                doctor.getId(),
                doctor.getUser().getEmail(),
                doctor.getUser().getFirstName(),
                doctor.getUser().getLastName(),
                doctor.getSpecialization(),
                new HashSet<>(),
                patientSimpleDtos
        );
        when(doctorService.addDoctor(doctor)).thenReturn(doctorDto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(doctor)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(doctorDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(doctorDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(doctorDto.getLastName()))
                .andExpect(jsonPath("$.specialization").value(doctorDto.getSpecialization()));
    }

    @Test
    void deleteDoctor_CorrectData_DoctorDeleted() throws Exception {
        // given
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void assignDoctorToFacility_CorrectData_ReturnUpdatedDoctor() throws Exception {
        // given
        Facility facility = new Facility(1L, "Hosp1", "Waw", "00-300", "Lecznicza", "11", new HashSet<>());
        Set<Facility> facilities = Set.of(facility);
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Patient patient = new Patient(1L, "23342", "500600700", LocalDate.of(1980, 1, 1), new HashSet<>(), user, new HashSet<>());
        Set<Patient> patients = new HashSet<>();
        patients.add(patient);
        Doctor doctor = new Doctor(1L, "Cardiologist", new HashSet<>(), user, facilities, patients);

        Set<PatientSimpleDto> patientSimpleDtos = patients.stream()
                .map(p -> new PatientSimpleDto(
                        p.getId(),
                        p.getUser().getFirstName(),
                        p.getUser().getLastName()))
                .collect(Collectors.toSet());

        DoctorDto doctorDto = new DoctorDto(
                doctor.getId(),
                doctor.getUser().getEmail(),
                doctor.getUser().getFirstName(),
                doctor.getUser().getLastName(),
                doctor.getSpecialization(),
                Set.of(1L),
                patientSimpleDtos
        );
        when(doctorService.assignDoctorToFacility(1L, 1L)).thenReturn(doctorDto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch("/doctors/1/facilities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(doctorDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(doctorDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(doctorDto.getLastName()))
                .andExpect(jsonPath("$.specialization").value(doctorDto.getSpecialization()))
                .andExpect(jsonPath("$.facilities[0]").value(1L));
    }
}

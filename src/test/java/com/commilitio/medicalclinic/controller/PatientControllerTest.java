package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.*;
import com.commilitio.medicalclinic.service.PatientService;
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
public class PatientControllerTest {

    @MockBean
    PatientService patientService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void getPatient_CorrectData_ReturnPatient() throws Exception {
        // given
        PatientDto patientDto = new PatientDto(1L, "bob@wp.pl", "23342", "Bob", "Kowalski", "500600700", LocalDate.of(1933, 11, 10), new HashSet<>());
        when(patientService.getPatient(1L)).thenReturn(patientDto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientDto.getId()))
                .andExpect(jsonPath("$.email").value(patientDto.getEmail()))
                .andExpect(jsonPath("$.idCardNo").value(patientDto.getIdCardNo()))
                .andExpect(jsonPath("$.firstName").value(patientDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patientDto.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(patientDto.getPhoneNumber()))
                .andExpect(jsonPath("$.birthdate").value(patientDto.getBirthdate().toString()));
    }

    @Test
    void getPatients_CorrectData_ReturnPatients() throws Exception {
        // given
        PatientDto patientDto1 = new PatientDto(1L, "bob@wp.pl", "23342", "Bob", "Kowalski", "500600700", LocalDate.of(1933, 11, 10), new HashSet<>());
        PatientDto patientDto2 = new PatientDto(2L, "jan@wp.pl", "22142", "Jan", "Kowalski", "500600701", LocalDate.of(1933, 11, 10), new HashSet<>());
        List<PatientDto> patients = List.of(patientDto1, patientDto2);
        when(patientService.getPatients(0, 10)).thenReturn(patients);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/patients")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(patientDto1.getId()))
                .andExpect(jsonPath("$[0].email").value(patientDto1.getEmail()))
                .andExpect(jsonPath("$[0].firstName").value(patientDto1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(patientDto1.getLastName()))
                .andExpect(jsonPath("$[0].phoneNumber").value(patientDto1.getPhoneNumber()))
                .andExpect(jsonPath("$[0].birthdate").value(patientDto1.getBirthdate().toString()))
                .andExpect(jsonPath("$[1].id").value(patientDto2.getId()))
                .andExpect(jsonPath("$[1].email").value(patientDto2.getEmail()))
                .andExpect(jsonPath("$[1].firstName").value(patientDto2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(patientDto2.getLastName()))
                .andExpect(jsonPath("$[1].phoneNumber").value(patientDto2.getPhoneNumber()))
                .andExpect(jsonPath("$[1].birthdate").value(patientDto2.getBirthdate().toString()));
    }

    @Test
    void addPatient_CorrectData_ReturnPatients() throws Exception {
        // given
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Set<Doctor> doctors = new HashSet<>();
        Patient patient = new Patient(1L, "23342", "500600700", LocalDate.of(1922, 11, 11), new HashSet<>(), user, doctors);

        Set<String> doctorStrings = doctors.stream()
                .map(doctor -> String.format("Doctor{id=%d, firstName='%s', lastName='%s', specialization='%s'}",
                        doctor.getId(),
                        doctor.getUser().getFirstName(),
                        doctor.getUser().getLastName(),
                        doctor.getSpecialization()))
                .collect(Collectors.toSet());

        PatientDto patientDto = new PatientDto(
                patient.getId(),
                patient.getUser().getEmail(),
                patient.getIdCardNo(),
                patient.getUser().getFirstName(),
                patient.getUser().getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthdate(),
                doctorStrings
        );
        when(patientService.addPatient(patient)).thenReturn(patientDto);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patient)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(patient.getId()))
                .andExpect(jsonPath("$.email").value(patient.getUser().getEmail()))
                .andExpect(jsonPath("$.firstName").value(patient.getUser().getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patient.getUser().getLastName()))
                .andExpect(jsonPath("$.idCardNo").value(patient.getIdCardNo()))
                .andExpect(jsonPath("$.phoneNumber").value(patient.getPhoneNumber()))
                .andExpect(jsonPath("$.birthdate").value(patient.getBirthdate().toString()));
    }

    @Test
    void deletePatient_CorrectData_PatientDeleted() throws Exception {
        // given
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());     // 204 No Content
    }

    @Test
    void updatePatient_CorrectData_ReturnUpdatedPatient() throws Exception {
        // given
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Set<Doctor> doctors = new HashSet<>();
        Patient patient = new Patient(1L, "23342", "500600700", LocalDate.of(1922, 11, 11), new HashSet<>(), user, doctors);

        Set<String> doctorStrings = doctors.stream()
                .map(doctor -> String.format("Doctor{id=%d, firstName='%s', lastName='%s', specialization='%s'}",
                        doctor.getId(),
                        doctor.getUser().getFirstName(),
                        doctor.getUser().getLastName(),
                        doctor.getSpecialization()))
                .collect(Collectors.toSet());

        PatientDto updatedPatient = new PatientDto(
                patient.getId(),
                "john@wp.pl",
                patient.getIdCardNo(),
                "John",
                patient.getUser().getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthdate(),
                doctorStrings
        );
        when(patientService.updatePatient(1L, patient)).thenReturn(updatedPatient);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patient)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(updatedPatient.getEmail()));
    }

    @Test
    void updatePassword_CorrectData_ReturnUpdatedPatient() throws Exception {
        // given
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Set<Doctor> doctors = new HashSet<>();
        Patient patient = new Patient(1L, "23342", "500600700", LocalDate.of(1922, 11, 11), new HashSet<>(), user, doctors);

        Set<String> doctorStrings = doctors.stream()
                .map(doctor -> String.format("Doctor{id=%d, firstName='%s', lastName='%s', specialization='%s'}",
                        doctor.getId(),
                        doctor.getUser().getFirstName(),
                        doctor.getUser().getLastName(),
                        doctor.getSpecialization()))
                .collect(Collectors.toSet());

        PatientDto updatedPatient = new PatientDto(
                patient.getId(),
                patient.getUser().getEmail(),
                patient.getIdCardNo(),
                patient.getUser().getFirstName(),
                patient.getUser().getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthdate(),
                doctorStrings
        );
        String newPass = "testyJednostkowe";
        when(patientService.updatePassword(1L, newPass)).thenReturn(updatedPatient);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPass))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void assignPatientToVisit_CorrectData_ReturnUpdatedPatient() throws Exception {
        // given
        PatientDto patientDto = new PatientDto(1L, "bob@wp.pl", "23342", "Bob", "Kowalski", "500600700", LocalDate.of(1933, 11, 10), new HashSet<>());
        when(patientService.assignPatientToVisit(1L, 1L)).thenReturn(patientDto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/1/visits/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(patientDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(patientDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patientDto.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(patientDto.getPhoneNumber()))
                .andExpect(jsonPath("$.birthdate").value(patientDto.getBirthdate().toString()));
    }
}

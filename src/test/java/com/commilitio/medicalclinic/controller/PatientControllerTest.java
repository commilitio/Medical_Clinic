package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import com.commilitio.medicalclinic.model.User;
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

import javax.print.attribute.standard.Media;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        PatientDto patientDto = new PatientDto(1L, "bob@wp.pl", "23342", "Bob", "Kowalski", "500600700", LocalDate.of(1933, 11, 10));
        when(patientService.getPatient(1L)).thenReturn(patientDto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(patientDto.getEmail()));
    }

    @Test
    void getPatients_CorrectData_ReturnPatients() throws Exception {
        // given
        PatientDto patientDto1 = new PatientDto(1L, "bob@wp.pl", "23342", "Bob", "Kowalski", "500600700", LocalDate.of(1933, 11, 10));
        PatientDto patientDto2 = new PatientDto(2L, "jan@wp.pl", "22142", "Jan", "Kowalski", "500600701", LocalDate.of(1933, 11, 10));
        List<PatientDto> patients = List.of(patientDto1, patientDto2);
        when(patientService.getPatients(0, 10)).thenReturn(patients);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/patients")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString())) // Debugowanie odpowiedzi JSON
                .andExpect(jsonPath("$[0].email").value(patientDto1.getEmail()))
                .andExpect(jsonPath("$[1].email").value(patientDto2.getEmail()));
    }

    @Test
    void addPatient_CorrectData_ReturnPatients() throws Exception {
        // given
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Patient patient = new Patient(1L, "23342", "500600700", LocalDate.of(1922, 11, 11), new HashSet<>(), user);
        PatientDto patientDto = new PatientDto(
                patient.getId(),
                patient.getUser().getEmail(),
                patient.getIdCardNo(),
                patient.getUser().getFirstName(),
                patient.getUser().getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthdate()
        );
        when(patientService.addPatient(patient)).thenReturn(patientDto);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patient)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(patient.getUser().getEmail()));
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
        Patient patient = new Patient(1L, "23342", "500600700", LocalDate.of(1922, 11, 11), new HashSet<>(), user);
        PatientDto updatedPatient = new PatientDto(
                patient.getId(),
                "john@wp.pl",
                patient.getIdCardNo(),
                "John",
                patient.getUser().getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthdate()
        );
        String patientJson = mapper.writeValueAsString(patient);
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
        Patient patient = new Patient(1L, "23342", "500600700", LocalDate.of(1922, 11, 11), new HashSet<>(), user);
        PatientDto updatedPatient = new PatientDto(
                patient.getId(),
                patient.getUser().getEmail(),
                patient.getIdCardNo(),
                patient.getUser().getFirstName(),
                patient.getUser().getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthdate()
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
}

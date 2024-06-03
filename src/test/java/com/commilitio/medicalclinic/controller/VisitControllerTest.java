package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.VisitCreateDto;
import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {

    @MockBean
    VisitService visitService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void getVisits_CorrectData_ReturnVisits() throws Exception {
        // given
        VisitDto visit1 = new VisitDto(1L, LocalDateTime.of(2007, 5, 15, 14, 0), LocalDateTime.of(2007, 5, 15, 15, 0), 1L, 1L);
        VisitDto visit2 = new VisitDto(2L, LocalDateTime.of(2013, 5, 16, 14, 0), LocalDateTime.of(2013, 5, 16, 15, 0), 2L, 2L);
        List<VisitDto> visits = List.of(visit1, visit2);
        Pageable pageable = PageRequest.of(0, 10);
        when(visitService.getVisits(pageable)).thenReturn(visits);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/visits")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(visit1.getId()))
                .andExpect(jsonPath("$[1].id").value(visit2.getId()))
                .andExpect(jsonPath("$[0].doctorId").value(visit1.getDoctorId()))
                .andExpect(jsonPath("$[0].patientId").value(visit1.getPatientId()))
                .andExpect(jsonPath("$[1].doctorId").value(visit2.getDoctorId()))
                .andExpect(jsonPath("$[1].patientId").value(visit2.getPatientId()));
    }

    @Test
    void createVisit_CorrectData_ReturnVisit() throws Exception {
        // given
        VisitCreateDto visitCreateDto = new VisitCreateDto(1L, LocalDateTime.of(2033, 5, 20, 10, 0), LocalDateTime.of(2033, 5, 20, 11, 0));
        VisitDto visitDto = new VisitDto(1L, visitCreateDto.getVisitStartTime(), visitCreateDto.getVisitEndTime(), 1L, 1L);
        when(visitService.createVisit(visitCreateDto)).thenReturn(visitDto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(visitCreateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(visitDto.getId()))
                .andExpect(jsonPath("$.doctorId").value(visitDto.getDoctorId()))
                .andExpect(jsonPath("$.patientId").value(visitDto.getPatientId()));
    }

}

package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.model.FacilityDto;
import com.commilitio.medicalclinic.service.FacilityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.HashSet;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FacilityControllerTest {

    @MockBean
    FacilityService facilityService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void getFacility_CorrectData_ReturnFacility() throws Exception {
        // given
        FacilityDto facility = new FacilityDto(1L, "Hosp", "Waw", "00-300", "Lecznicza", "11", new HashSet<>());
        when(facilityService.getFacility(1L)).thenReturn(facility);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/facilities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(facility.getName()))
                .andExpect(jsonPath("$.zipCode").value(facility.getZipCode()))
                .andExpect(jsonPath("$.streetName").value(facility.getStreetName()))
                .andExpect(jsonPath("$.streetNumber").value(facility.getStreetNumber()));
    }

    @Test
    void getFacilities_CorrectData_ReturnFacilities() throws Exception {
        // given
        FacilityDto facility1 = new FacilityDto(1L, "Hosp1", "Waw", "00-300", "Lecznicza", "11", new HashSet<>());
        FacilityDto facility2 = new FacilityDto(2L, "Hosp2", "Krk", "30-300", "Zabawna", "7", new HashSet<>());
        List<FacilityDto> facilities = List.of(facility1, facility2);
        when(facilityService.getFacilities(0, 15)).thenReturn(facilities);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/facilities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(facility1.getName()))
                .andExpect(jsonPath("$[1].name").value(facility2.getName()))
                .andExpect(jsonPath("$[0].city").value(facility1.getCity()))
                .andExpect(jsonPath("$[1].city").value(facility2.getCity()))
                .andExpect(jsonPath("$[0].zipCode").value(facility1.getZipCode()))
                .andExpect(jsonPath("$[1].zipCode").value(facility2.getZipCode()))
                .andExpect(jsonPath("$[0].streetName").value(facility1.getStreetName()))
                .andExpect(jsonPath("$[1].streetName").value(facility2.getStreetName()));
    }

    @Test
    void addFacility_CorrectData_ReturnFacility() throws Exception {
        // given
        Facility facility = new Facility(1L, "Hosp1", "Waw", "00-300", "Lecznicza", "11", new HashSet<>());
        FacilityDto facilityDto = new FacilityDto(
                facility.getId(),
                facility.getName(),
                facility.getCity(),
                facility.getZipCode(),
                facility.getStreetName(),
                facility.getStreetNumber(),
                new HashSet<>());
        when(facilityService.addFacility(facility)).thenReturn(facilityDto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/facilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(facility)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(facility.getName()))
                .andExpect(jsonPath("$.city").value(facility.getCity()))
                .andExpect(jsonPath("$.zipCode").value(facility.getZipCode()))
                .andExpect(jsonPath("$.streetName").value(facility.getStreetName()))
                .andExpect(jsonPath("$.streetNumber").value(facility.getStreetNumber()));
    }

    @Test
    void deleteFacility_CorrectData_FacilityDeleted() throws Exception {
        // given
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/facilities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

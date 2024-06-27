package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.DoctorDto;
import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Get a doctor by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the doctor.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Doctor Not Found.",
                    content = @Content) })
    @GetMapping("/{id}")
    public DoctorDto getDoctor(@Parameter(description = "id of doctor to be searched") @PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @Operation(summary = "Get a list of doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the doctors.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class)) }),
            @ApiResponse(responseCode = "404", description = "No doctors found.",
                    content = @Content) })
    @GetMapping
    public List<DoctorDto> getDoctors(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return doctorService.getDoctors(page, size);
    }

    @Operation(summary = "Get a list of doctor available visits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the doctors.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Doctor not found.",
                    content = @Content) })
    @GetMapping("/{id}/visits")
    List<VisitDto> getDoctorAvailableVisits(@PathVariable Long id) {
        return doctorService.getDoctorAvailableVisits(id);
    }

    @Operation(summary = "Add a new doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class)) }),
            @ApiResponse(responseCode = "409", description = "Doctor already exists.",
                    content = @Content) })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @Operation(summary = "Delete a doctor by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor deleted.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Doctor Not Found.",
                    content = @Content) })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@Parameter(description = "id of doctor to be deleted") @PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @Operation(summary = "Assign certain doctor to a certain facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor assigned to facility.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Doctor or Facility Not Found.",
                    content = @Content) })
    @PatchMapping("/{doctorId}/facilities/{facilityId}")
    public DoctorDto assignDoctorToFacility(
            @Parameter(description = "id of the doctor to be assigned") @PathVariable Long doctorId,
            @Parameter(description = "id of the facility to assign the doctor to") @PathVariable Long facilityId) {
        return doctorService.assignDoctorToFacility(doctorId, facilityId);
    }
}

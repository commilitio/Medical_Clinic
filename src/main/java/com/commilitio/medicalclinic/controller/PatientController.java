package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.Patient;
import com.commilitio.medicalclinic.model.PatientDto;
import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.service.PatientService;
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
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Get a list of patients with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of patients",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "404", description = "No patients found",
                    content = @Content)})
    @GetMapping
    public List<PatientDto> getPatients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return patientService.getPatients(page, size);
    }

    @Operation(summary = "Get a patient by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the patient",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient Not Found",
                    content = @Content)})
    @GetMapping("/{id}")
    public PatientDto getPatient(@Parameter(description = "id of patient to be searched") @PathVariable Long id) {
        return patientService.getPatient(id);
    }

    @Operation(summary = "Get patient visits list by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found patients list",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient Not Found",
                    content = @Content)})
    @GetMapping("/{id}/visits")
    public List<VisitDto> getPatientVisits(@Parameter(description = "id of patient to be searched") @PathVariable Long id) {
        return patientService.getPatientVisits(id);
    }

    @Operation(summary = "Add a new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "409", description = "Patient already exists",
                    content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @Operation(summary = "Delete a patient by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient Not Found",
                    content = @Content)})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@Parameter(description = "id of patient to be deleted") @PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @Operation(summary = "Update a patient by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient Not Found",
                    content = @Content)})
    @PutMapping("/{id}")
    public PatientDto updatePatient(@Parameter(description = "id of patient to be updated") @PathVariable Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @Operation(summary = "Update a patient's password by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient's password updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient Not Found",
                    content = @Content)})
    @PatchMapping("/{id}/password")
    public PatientDto updatePassword(@Parameter(description = "id of patient whose password has to be updated") @PathVariable Long id, @RequestBody String password) {
        return patientService.updatePassword(id, password);
    }

    @Operation(summary = "Assign certain patient to a certain visit by his id and visit id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient assigned to visit",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient or Visit Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Chosen visit has already expired",
                    content = @Content)})
    @PatchMapping("/{id}/visits/{visitId}")
    public PatientDto assignPatientToVisit(@Parameter(description = "id of patient to be assigned") @PathVariable Long id, @Parameter(description = "id of visit to be assigned") @PathVariable Long visitId) {
        return patientService.assignPatientToVisit(id, visitId);
    }
}

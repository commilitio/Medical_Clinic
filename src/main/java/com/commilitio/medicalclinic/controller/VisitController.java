package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.VisitCreateDto;
import com.commilitio.medicalclinic.model.VisitDto;
import com.commilitio.medicalclinic.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    @Operation(summary = "Get a list of visits with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of visits",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VisitDto.class)) })})
    @GetMapping
    public List<VisitDto> getVisits(Pageable pageable) {
        return visitService.getVisits(pageable);
    }

    @Operation(summary = "Get a list of available visits with a certain doctor's specialization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of visits",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VisitDto.class)) })})
    @GetMapping("/specialization")
    public List<VisitDto> getVisitsBySpecialization(@RequestParam LocalDate visitDate, @RequestParam String specialization) {
        return visitService.getVisitsBySpecialization(visitDate, specialization);
    }

    @Operation(summary = "Create a new visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Visit created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VisitDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data or visit has already expired",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "The doctor's visits is already occupied at the specified time",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Doctor Not Found",
                    content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDto createVisit(@RequestBody VisitCreateDto visitCreateDto) {
        return visitService.createVisit(visitCreateDto);
    }
}

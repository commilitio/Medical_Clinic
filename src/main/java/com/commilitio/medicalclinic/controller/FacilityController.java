package com.commilitio.medicalclinic.controller;

import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.model.FacilityDto;
import com.commilitio.medicalclinic.service.FacilityService;
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
@RequestMapping("/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    @Operation(summary = "Get a facility by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the facility",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDto.class))}),
            @ApiResponse(responseCode = "404", description = "Facility Not Found",
                    content = @Content)})
    @GetMapping("/{id}")
    public FacilityDto getFacility(@Parameter(description = "id of facility to be searched") @PathVariable Long id) {
        return facilityService.getFacility(id);
    }

    @Operation(summary = "Get a list of facilities with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of facilities",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDto.class))})})
    @GetMapping
    public List<FacilityDto> getFacilities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return facilityService.getFacilities(page, size);
    }

    @Operation(summary = "Add a new facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Facility created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDto.class))}),
            @ApiResponse(responseCode = "409", description = "Facility already exists",
                    content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityDto addFacility(@RequestBody Facility facility) {
        return facilityService.addFacility(facility);
    }

    @Operation(summary = "Delete a facility by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Facility deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Facility Not Found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFacility(@Parameter(description = "id of facility to be deleted") @PathVariable Long id) {
        facilityService.deleteFacility(id);
    }
}
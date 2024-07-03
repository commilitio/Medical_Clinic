package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.exception.DoctorException;
import com.commilitio.medicalclinic.exception.FacilityException;
import com.commilitio.medicalclinic.mapper.DoctorMapper;
import com.commilitio.medicalclinic.mapper.VisitMapper;
import com.commilitio.medicalclinic.model.*;
import com.commilitio.medicalclinic.repository.DoctorRepository;
import com.commilitio.medicalclinic.repository.FacilityRepository;
import com.commilitio.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;
    private final VisitMapper visitMapper;

    public DoctorDto getDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorException("Doctor Not Found.", HttpStatus.NOT_FOUND));
        return doctorMapper.toDto(doctor);
    }

    public List<DoctorDto> getDoctors(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        if (doctors.isEmpty()) {
            throw new DoctorException("No doctors found.", HttpStatus.NOT_FOUND);
        }
        return doctorMapper.toDtos(doctors.toList());
    }

    public List<VisitDto> getDoctorAvailableVisits(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorException("Doctor Not Found.", HttpStatus.NOT_FOUND));

        return doctor.getVisits().stream()
                .filter(visit -> visit.getPatient() == null)
                .map(visitMapper::toDto)
                .toList();
    }

    @Transactional
    public DoctorDto addDoctor(Doctor doctor) {
        boolean doctorExists = userRepository
                .findPatientByEmail(doctor.getUser().getEmail())
                .isPresent();
        if (doctorExists) {
            throw new DoctorException("Doctor already exists.", HttpStatus.CONFLICT);
        }
        User user = User.builder()
                .firstName(doctor.getUser().getFirstName())
                .lastName(doctor.getUser().getLastName())
                .email(doctor.getUser().getEmail())
                .password(doctor.getUser().getPassword())
                .build();

        doctor.setUser(user);
        Doctor addedDoctor = doctorRepository.save(doctor);
        log.info("Created doctor with data: {}", doctor);
        return doctorMapper.toDto(addedDoctor);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorException("Doctor Not Found.", HttpStatus.NOT_FOUND));
        doctorRepository.delete(doctor);
        log.info("Deleted doctor with id: {}", id);
    }

    @Transactional
    public DoctorDto assignDoctorToFacility(Long doctorId, Long facilityId) {
        Doctor doctorToAssign = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorException("Doctor Not Found.", HttpStatus.NOT_FOUND));
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new FacilityException("Facility Not Found.", HttpStatus.NOT_FOUND));

        doctorToAssign.getFacilities().add(facility);
        doctorRepository.save(doctorToAssign);
        log.info("Assigned doctor with data: {} to facility with data: {}", doctorToAssign, facility);
        return doctorMapper.toDto(doctorToAssign);
    }
}

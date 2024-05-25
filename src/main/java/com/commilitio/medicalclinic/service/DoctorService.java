package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.mapper.DoctorMapper;
import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.DoctorDto;
import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.repository.DoctorRepository;
import com.commilitio.medicalclinic.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;
    private final DoctorMapper doctorMapper;

    public DoctorDto getDoctor(String email) {
        Doctor doctor = doctorRepository.findDoctorByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Doctor Not Found."));
        return doctorMapper.toDto(doctor);
    }

    public List<DoctorDto> getDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctorMapper.toDtos(doctors);
    }

    @Transactional
    public DoctorDto addDoctor(Doctor doctor) {
        boolean doctorExists = doctorRepository
                .findDoctorByEmail(doctor.getEmail())
                .isPresent();
        if (doctorExists) {
            throw new IllegalArgumentException("Doctor already exists.");
        }
        Doctor addedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(addedDoctor);
    }

    public void deleteDoctor(String email) {
        Doctor doctor = doctorRepository.findDoctorByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Doctor Not Found."));
        doctorRepository.delete(doctor);
    }

    @Transactional
    public DoctorDto assignDoctorToFacility(Long doctor_id, Long facility_id){
        Doctor doctorToAssign = doctorRepository.findById(doctor_id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor Not Found."));
        Facility facility = facilityRepository.findById(facility_id)
                .orElseThrow(() -> new IllegalArgumentException("Facility Not Found."));

        doctorToAssign.getFacilities().add(facility);
        doctorRepository.save(doctorToAssign);
        return doctorMapper.toDto(doctorToAssign);
    }
}

package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.mapper.DoctorMapper;
import com.commilitio.medicalclinic.model.Doctor;
import com.commilitio.medicalclinic.model.DoctorDto;
import com.commilitio.medicalclinic.model.Facility;
import com.commilitio.medicalclinic.model.User;
import com.commilitio.medicalclinic.repository.DoctorRepository;
import com.commilitio.medicalclinic.repository.FacilityRepository;
import com.commilitio.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;

    public DoctorDto getDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor Not Found."));
        return doctorMapper.toDto(doctor);
    }

    public List<DoctorDto> getDoctors(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        return doctorMapper.toDtos(doctors.toList());
    }

    @Transactional
    public DoctorDto addDoctor(Doctor doctor) {
        boolean doctorExists = userRepository
                .findPatientByEmail(doctor.getUser().getEmail())
                .isPresent();
        if (doctorExists) {
            throw new IllegalArgumentException("Doctor already exists.");
        }
        User user = new User();
        user.setFirstName(doctor.getUser().getFirstName());
        user.setLastName(doctor.getUser().getLastName());
        user.setEmail(doctor.getUser().getEmail());
        user.setPassword(doctor.getUser().getPassword());

        doctor.setUser(user);
        Doctor addedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(addedDoctor);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor Not Found."));
        doctorRepository.delete(doctor);
    }

    @Transactional
    public DoctorDto assignDoctorToFacility(Long doctorId, Long facilityId){
        Doctor doctorToAssign = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor Not Found."));
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility Not Found."));

        doctorToAssign.getFacilities().add(facility);
        doctorRepository.save(doctorToAssign);
        return doctorMapper.toDto(doctorToAssign);
    }
}

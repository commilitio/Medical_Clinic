package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.exception.DoctorException;
import com.commilitio.medicalclinic.exception.FacilityException;
import com.commilitio.medicalclinic.mapper.DoctorMapper;
import com.commilitio.medicalclinic.model.*;
import com.commilitio.medicalclinic.repository.DoctorRepository;
import com.commilitio.medicalclinic.repository.FacilityRepository;
import com.commilitio.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {

    private DoctorService doctorService;
    private DoctorRepository doctorRepository;
    private FacilityRepository facilityRepository;
    private DoctorMapper doctorMapper;
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.facilityRepository = Mockito.mock(FacilityRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);

        this.doctorService = new DoctorService(doctorRepository, facilityRepository, doctorMapper, userRepository);
    }

    @Test
    void getDoctor_DoctorExists_ReturnDoctor() {
        // given
        User user = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Doctor doctor = new Doctor(1L, "Cardiology", new HashSet<>(), user, new HashSet<>());
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        // when
        DoctorDto result = doctorService.getDoctor(1L);
        // then
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("jk@wp.pl", result.getEmail());
        Assertions.assertEquals("Jan", result.getFirstName());
        Assertions.assertEquals("Kowal", result.getLastName());
        Assertions.assertEquals("Cardiology", result.getSpecialization());
    }

    @Test
    void getDoctor_DoctorDoesNotExists_ThrowsIllegalArgumentException() {
        // given
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(DoctorException.class, () -> doctorService.getDoctor(1L));
        // then
        Assertions.assertEquals("Doctor Not Found.", result.getMessage());
    }

    @Test
    void getDoctors_DoctorsExist_ReturnDoctors() {
        // given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Doctor doctor1 = new Doctor(1L, "Cardiology", new HashSet<>(), new User(), new HashSet<>());
        Doctor doctor2 = new Doctor(2L, "Pulmonology", new HashSet<>(), new User(), new HashSet<>());
        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);
        Page<Doctor> doctorPage = new PageImpl<>(doctors, pageable, doctors.size());
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);
        // when
        var result = doctorService.getDoctors(pageNumber, pageSize);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, doctor1.getId());
        Assertions.assertEquals(2L, doctor2.getId());
        Assertions.assertEquals("Cardiology", doctor1.getSpecialization());
        Assertions.assertEquals("Pulmonology", doctor2.getSpecialization());
    }

    @Test
    void addDoctor_DoctorAlreadyExists_ThrowsIllegalArgumentException() {
        // given
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Doctor doctor = new Doctor(1L, "Proctology", new HashSet<>(), user, new HashSet<>());
        when(userRepository.findPatientByEmail("bob@wp.pl")).thenReturn(Optional.of(user));
        // when
        Exception result = Assertions.assertThrows(DoctorException.class, () -> doctorService.addDoctor(doctor));
        // then
        Assertions.assertEquals("Doctor already exists.", result.getMessage());
    }

    @Test
    void addDoctor_CorrectData_ReturnDoctor() {
        // given
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Doctor doctor = new Doctor(1L, "Surgery", new HashSet<>(), user, new HashSet<>());
        when(userRepository.findPatientByEmail("bob@wp.pl")).thenReturn(Optional.empty());
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        // when
        DoctorDto result = doctorService.addDoctor(doctor);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(doctorMapper.toDto(doctor), result);
    }

    @Test
    void deleteDoctor_DoctorDoesNotExist_ThrowsIllegalArgumentException() {
        // given
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(DoctorException.class, () -> doctorService.deleteDoctor(1L));
        // then
        Assertions.assertEquals("Doctor Not Found.", result.getMessage());
    }

    @Test
    void deleteDoctor_DoctorExists_DoctorDeleted() {
        // given
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Doctor doctor = new Doctor(1L, "Surgery", new HashSet<>(), user, new HashSet<>());
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        // when
        doctorService.deleteDoctor(1L);
        // then
        verify(doctorRepository).delete(doctor);
    }

    @Test
    void assignDoctorToFacility_DoctorAndFacilityExist_ReturnAssignedDoctor() {
        // given
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Doctor doctor = new Doctor(1L, "Surgery", new HashSet<>(), user, new HashSet<>());
        Facility facility = new Facility(1L, "Hospital", "Waw", "009111", "ul Osa", "22", new HashSet<>());
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility));
        // when
        DoctorDto result = doctorService.assignDoctorToFacility(1L, 1L);
        // then
        Assertions.assertEquals(doctor.getSpecialization(), result.getSpecialization());
        Assertions.assertEquals("Bob", result.getFirstName());
        Assertions.assertEquals("Budowniczy", result.getLastName());
        Assertions.assertEquals("bob@wp.pl", result.getEmail());
        Assertions.assertEquals(1, result.getFacilities().size());
    }

    @Test
    void assignDoctorToFacility_FacilityDoesNotExist_ThrowsIllegalArgumentException() {
        // given
        User user = new User(1L, "Bob", "Budowniczy", "bob@wp.pl", "pass1");
        Doctor doctor = new Doctor(1L, "Surgery", new HashSet<>(), user, new HashSet<>());
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(facilityRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(FacilityException.class, () -> doctorService.assignDoctorToFacility(1L, 1L));
        // then
        Assertions.assertEquals("Facility Not Found.", result.getMessage());
    }
}

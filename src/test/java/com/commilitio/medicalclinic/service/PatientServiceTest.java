package com.commilitio.medicalclinic.service;

import com.commilitio.medicalclinic.exception.PatientException;
import com.commilitio.medicalclinic.exception.VisitException;
import com.commilitio.medicalclinic.mapper.PatientMapper;
import com.commilitio.medicalclinic.model.*;
import com.commilitio.medicalclinic.repository.DoctorRepository;
import com.commilitio.medicalclinic.repository.PatientRepository;
import com.commilitio.medicalclinic.repository.UserRepository;
import com.commilitio.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceTest {

    private PatientService patientService;
    private PatientRepository patientRepository;
    private PatientMapper patientMapper;
    private VisitRepository visitRepository;
    private UserRepository userRepository;
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);

        this.patientService = new PatientService(patientRepository, patientMapper, visitRepository, userRepository, doctorRepository);
    }

    @Test
    void getPatient_PatientExist_ReturnPatient() {
        // given
        User user = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user, new HashSet<>());
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        // when
        PatientDto result = patientService.getPatient(1L);
        // then
        Assertions.assertEquals(patientMapper.toDto(patient), result);
    }

    @Test
    void getPatient_PatientDoesNotExist_ThrowsIllegalArgumentException() {
        // given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(PatientException.class, () -> patientService.getPatient(1L));
        // then
        Assertions.assertEquals("Patient Not Found.", result.getMessage());
    }

    @Test
    void getPatients_PatientsExist_ReturnPatients() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        User user1 = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient1 = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user1, new HashSet<>());
        User user2 = new User(2L, "Bob", "Kowal", "bob@wp.pl", "pass2");
        Patient patient2 = new Patient(2L, "234234", "100-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user2, new HashSet<>());
        List<Patient> patients = Arrays.asList(patient1, patient2);
        Page<Patient> doctorPage = new PageImpl<>(patients, pageable, patients.size());
        when(patientRepository.findAll(pageable)).thenReturn(doctorPage);
        // when
        var result = patientService.getPatients(pageNumber, pageSize);
        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void getPatients_PatientsDoNotExist_ThrowsIllegalArgumentException() {
        //given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Patient> emptyPage = new PageImpl<>(Collections.emptyList());
        when(patientRepository.findAll(pageable)).thenReturn(emptyPage);
        // when
        Exception result = Assertions.assertThrows(PatientException.class, () -> patientService.getPatients(pageNumber, pageSize));
        // then
        Assertions.assertEquals("No patients found.", result.getMessage());
    }

    @Test
    void addPatient_PatientAlreadyExists_ThrowsIllegalArgumentException() {
        // given
        User user = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user, new HashSet<>());
        when(userRepository.findPatientByEmail("jk@wp.pl")).thenReturn(Optional.of(user));
        // when
        Exception result = Assertions.assertThrows(PatientException.class, () -> patientService.addPatient(patient));
        // then
        Assertions.assertEquals("Patient already exists.", result.getMessage());
    }

    @Test
    void addPatient_CorrectData_ReturnPatient() {
        // given
        User user = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user, new HashSet<>());
        when(userRepository.findPatientByEmail("jk@wp.pl")).thenReturn(Optional.empty());
        when(patientRepository.save(patient)).thenReturn(patient);
        // when
        PatientDto result = patientService.addPatient(patient);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Jan", result.getFirstName());
        Assertions.assertEquals("Kowal", result.getLastName());
        Assertions.assertEquals("141324", result.getIdCardNo());
        Assertions.assertEquals(LocalDate.of(2001, 11, 13), result.getBirthdate());
        Assertions.assertEquals("300-400-500", result.getPhoneNumber());
    }

    @Test
    void deletePatient_PatientDoesNotExist_ThrowsIllegalArgumentException() {
        // given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(PatientException.class, () -> patientService.deletePatient(1L));
        // then
        Assertions.assertEquals("Patient Not Found.", result.getMessage());
    }

    @Test
    void deletePatient_CorrectData_PatientDeleted() {
        // given
        User user = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user, new HashSet<>());
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        // when
        patientService.deletePatient(1L);
        // then
        verify(patientRepository).delete(patient);
    }

    @Test
    void updatePatient_PatientDoesNotExist_ThrowsIllegalArgumentException() {
        // given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(PatientException.class, () -> patientService.updatePatient(1L, new Patient()));
        // then
        Assertions.assertEquals("Patient Not Found.", result.getMessage());
    }

    @Test
    void updatePatient_CorrectData_ReturnUpdatedPatient() {
        // given
        User user = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user, new HashSet<>());
        User newUser = new User(1L, "Bob", "Kowal", "bob@wp.pl", "pass1");
        Patient newPatient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), newUser, new HashSet<>());

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        // when
        PatientDto result = patientService.updatePatient(1L, newPatient);
        // then
        Assertions.assertEquals("Bob", result.getFirstName());
        Assertions.assertEquals("bob@wp.pl", result.getEmail());
    }

    @Test
    void updatePassword_PatientDoesNotExist_ThrowsIllegalArgumentException() {
        // given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(PatientException.class, () -> patientService.updatePassword(1L, "newPass"));
        // then
        Assertions.assertEquals("Patient Not Found.", result.getMessage());
    }

    @Test
    void updatePassword_CorrectData_ReturnPatient() {
        // given
        User user = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user, new HashSet<>());
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        // when
        patientService.updatePassword(1L, "newPass");
        // then
        Assertions.assertEquals("newPass", patient.getUser().getPassword());
        verify(patientRepository).save(patient);
    }

    @Test
    void assignPatientToVisit_VisitDoNotExist_ThrowsIllegalArgumentException() {
        // given
        User user = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user, new HashSet<>());
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(visitRepository.findVisitById(1L)).thenReturn(Optional.empty());
        // when
        Exception result = Assertions.assertThrows(VisitException.class, () -> patientService.assignPatientToVisit(1L, 1L));
        // then
        Assertions.assertEquals("Visit Not Found.", result.getMessage());
    }

    @Test
    void assignPatientToVisit_VisitHasExpired_ThrowsIllegalArgumentException() {
        // given
        User user = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), user, new HashSet<>());
        Doctor doctor = new Doctor();
        LocalDateTime visitStartTime = LocalDateTime.of(2011, 11,10, 18, 15);
        LocalDateTime visitEndTime = LocalDateTime.of(2011, 11,10, 18, 30);
        Visit visit = new Visit(1L, visitStartTime, visitEndTime, null, doctor);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(visitRepository.findVisitById(1L)).thenReturn(Optional.of(visit));
        // when
        Exception result = Assertions.assertThrows(IllegalArgumentException.class, () -> patientService.assignPatientToVisit(1L, 1L));
        // then
        Assertions.assertEquals("Chosen visit has already expired.", result.getMessage());
    }

    @Test
    void assignPatientToVisit_CorrectData_ReturnPatient() {
        // given
        User patientUser = new User(1L, "Jan", "Kowal", "jk@wp.pl", "pass1");
        Patient patient = new Patient(1L, "141324", "300-400-500", LocalDate.of(2001, 11, 13), new HashSet<>(), patientUser, new HashSet<>());

        User doctorUser = new User(2L, "Adam", "Nowak", "an@wp.pl", "pass2");
        Doctor doctor = new Doctor(1L, "Cardiologist", new HashSet<>(), doctorUser, new HashSet<>(), new HashSet<>());

        LocalDateTime visitStartTime = LocalDateTime.of(2026, 11,10, 18, 15);
        LocalDateTime visitEndTime = LocalDateTime.of(2026, 11,10, 18, 30);
        Visit visit = new Visit(1L, visitStartTime, visitEndTime, null, doctor);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(visitRepository.findVisitById(1L)).thenReturn(Optional.of(visit));
        // when
        PatientDto result = patientService.assignPatientToVisit(1L, 1L);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Jan", result.getFirstName());
        Assertions.assertEquals("Kowal", result.getLastName());
        Assertions.assertEquals("141324", result.getIdCardNo());
        Assertions.assertEquals(LocalDate.of(2001, 11, 13), result.getBirthdate());
        Assertions.assertEquals("300-400-500", result.getPhoneNumber());
        Assertions.assertEquals(patient, visit.getPatient());
        verify(visitRepository).save(visit);
    }
}

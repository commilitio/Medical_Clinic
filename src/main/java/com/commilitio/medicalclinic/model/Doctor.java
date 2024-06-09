package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DOCTOR")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "SPECIALIZATION")
    private String specialization;
    @OneToMany(mappedBy = "doctor")
    private Set<Visit> visits = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "DOCTOR_FACILITY",
            joinColumns = {@JoinColumn(name = "DOCTOR_ID")},
            inverseJoinColumns = {@JoinColumn(name = "FACILITY_ID")}
    )
    private Set<Facility> facilities = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Doctor))
            return false;

        Doctor other = (Doctor) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", user=" + user +
                ", specialization='" + specialization + '\'' +
                ", facilities=" + facilities.stream()
                .map(Facility::getId)
                .collect(Collectors.toSet()) +
                '}';
    }
}

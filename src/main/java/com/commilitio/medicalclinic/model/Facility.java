package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FACILITY")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 40)
    @Column(nullable = false, name = "NAME", unique = true)
    private String name;
    @Column(nullable = false, name = "CITY")
    private String city;
    @Column(nullable = false, name = "ZIP_CODE")
    private String zipCode;
    @Column(nullable = false, name = "STREET_NAME")
    private String streetName;
    @Column(nullable = false, name = "STREET_NUMBER")
    private String streetNumber;
    @ManyToMany(mappedBy = "facilities")
    private Set<Doctor> doctors = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Facility))
            return false;

        Facility other = (Facility) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", streetName='" + streetName + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", doctors=" + doctors.stream()
                .map(Doctor::getId)
                .collect(Collectors.toSet()) +
                '}';
    }
}

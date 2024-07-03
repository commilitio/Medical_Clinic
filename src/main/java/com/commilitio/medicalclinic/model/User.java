package com.commilitio.medicalclinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, max = 20)
    @Column(nullable = false, name = "FIRST_NAME")
    private String firstName;
    @Size(min = 2, max = 30)
    @Column(nullable = false, name = "LAST_NAME")
    private String lastName;
    @Column(nullable = false, unique = true, name = "EMAIL")
    private String email;
    @Size(min = 8)
    @Column(nullable = false, name = "PASSWORD")
    private String password;
}

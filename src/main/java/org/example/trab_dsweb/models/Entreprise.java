package org.example.trab_dsweb.models;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.trab_dsweb.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "entreprise")
@Getter
public class Entreprise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    private boolean admin;
    private String email;
    private String password;
    private String cpf;
    private String name;
    private Gender gender;
    private LocalDate birthDate;

}

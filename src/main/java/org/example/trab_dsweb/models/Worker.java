package org.example.trab_dsweb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trab_dsweb.enums.Gender;
import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "workers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthDate;

    public Worker(String email, String cpf, String password, String name, Gender gender, LocalDate birthDate) {
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
    }
}

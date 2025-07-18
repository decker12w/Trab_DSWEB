package org.example.trab_dsweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trab_dsweb.indicator.Gender;
import org.example.trab_dsweb.validator.UniqueCPF;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "workers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @UniqueCPF(message = "{Unique.worker.CPF}")
    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @OneToMany(mappedBy = "worker", orphanRemoval = true)
    private Set<JobApplication> jobApplications;
}

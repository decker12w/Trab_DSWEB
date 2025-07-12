package org.example.trab_dsweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trab_dsweb.validator.UniqueCNPJ;


import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "enterprises")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @UniqueCNPJ(message = "{Unique.enterprise.CNPJ}")
    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String city;

    @OneToMany(mappedBy = "enterprise", orphanRemoval = true)
    private List<Job> jobs;
}

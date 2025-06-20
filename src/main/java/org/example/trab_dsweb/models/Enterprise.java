package org.example.trab_dsweb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "enterprise")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String cnpj; // Changed to lowercase

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String city;

    public Enterprise(String email, String password, String cnpj, String name, String description, String city) {
        this.email = email;
        this.password = password;
        this.cnpj = cnpj;
        this.name = name;
        this.description = description;
        this.city = city;
    }
}
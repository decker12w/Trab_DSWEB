package org.example.trab_dsweb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trab_dsweb.enums.JobType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String CNPJ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @ManyToOne
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Entreprise entreprise;

    @Column(nullable = false)
    private LocalDateTime applicationDeadline;

    @Column(nullable = false)
    private boolean jobActive;

    @Column(nullable = false)
    private String city;
}
package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.enums.JobType;
import org.example.trab_dsweb.models.Entreprise; // Importar se a classe Entreprise for diretamente retornada
import java.time.LocalDateTime;
import java.util.UUID;

public record GetJobResponseDTO (
        UUID id,
        String description,
        String CNPJ,
        JobType jobType,
        Entreprise entreprise,
        LocalDateTime applicationDeadline,
        boolean jobActive,
        String city
) {
}
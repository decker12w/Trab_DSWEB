package org.example.trab_dsweb.dto;

import java.util.UUID;

public record CreateEnterpriseResponseDTO(
        UUID id,
        String email,
        String CNPJ,
        String name,
        String description,
        String city,
        ) {}
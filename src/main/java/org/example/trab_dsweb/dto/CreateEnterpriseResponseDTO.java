package org.example.trab_dsweb.dto;

import java.util.UUID;

public record CreateEnterpriseResponseDTO(
        UUID id,
        String name,
        String email,
        String cnpj,
        String description
) {}


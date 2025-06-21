package org.example.trab_dsweb.dto;

import java.util.UUID;

public record ReturnEnterpriseDTO(
        UUID id,
        String email,
        String cnpj,
        String name,
        String description,
        String city
        ) {}
package org.example.trab_dsweb.dto;

public record CreateEnterpriseDTO(
        String email,
        String password,
        String cnpj,
        String name,
        String description,
        String city
) {}
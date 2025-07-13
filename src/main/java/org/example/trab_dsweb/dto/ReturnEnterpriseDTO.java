package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.model.Enterprise;
import org.example.trab_dsweb.model.Worker;

import java.util.UUID;

public record ReturnEnterpriseDTO(
        UUID id,
        String email,
        String cnpj,
        String name,
        String description,
        String city
) {
    public ReturnEnterpriseDTO(Enterprise enterprise) {
        this(
                enterprise.getId(),
                enterprise.getEmail(),
                enterprise.getCnpj(),
                enterprise.getName(),
                enterprise.getDescription(),
                enterprise.getCity()
        );
    }
}
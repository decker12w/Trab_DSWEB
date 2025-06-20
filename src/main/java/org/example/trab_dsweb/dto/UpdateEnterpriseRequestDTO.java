package org.example.trab_dsweb.dto;

public record UpdateEnterpriseDTO(
        String email,
        String password,
        String CNPJ,
        String name,
        String description,
        String city
){
}

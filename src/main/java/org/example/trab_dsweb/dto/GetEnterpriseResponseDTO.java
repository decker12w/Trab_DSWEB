package org.example.trab_dsweb.dto;

public record GetEnterpriseDTO(
        String email,
        String password,
        String CNPJ,
        String name,
        String description,
        String city
){
}

package org.example.trab_dsweb.dto;

public record GetEnterpriseDTO(
        String email,
        String password,
        String cnpj,
        String description,
        String city
){
}

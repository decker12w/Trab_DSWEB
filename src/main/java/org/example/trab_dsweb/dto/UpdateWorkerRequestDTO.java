package org.example.trab_dsweb.dto;

public record UpdateWorkerRequestDTO(
        String email,
        String password,
        String cpf,
        String name
){
}

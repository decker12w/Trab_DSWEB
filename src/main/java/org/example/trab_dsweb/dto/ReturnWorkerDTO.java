package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record ReturnWorkerDTO(
        UUID id,
        String email,
        String cpf,
        String name,
        Gender gender,
        LocalDate birthDate){
}

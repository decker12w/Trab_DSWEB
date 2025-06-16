package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record CreateWorkerRequestDTO(
        String email,
        String password,
        String cpf,
        String name,
        Gender gender,
        LocalDate birthDate
) {}
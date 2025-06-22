package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.trab_dsweb.enums.Gender;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CreateWorkerDTO(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotBlank
        @CPF
        String cpf,

        @NotBlank
        String name,

        @NotNull
        Gender gender,

        @NotNull
        LocalDate birthDate
) {}
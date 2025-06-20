package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record CreateEnterpriseDTO(

        @Email
        String email,

        @NotBlank
        String password,

        @CNPJ
        String cnpj,

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String city
) {}
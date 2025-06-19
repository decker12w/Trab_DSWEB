package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record CreateEnterpriseRequestDTO(

        String name,


        String email,


        String password,


        String cnpj,


        String description
) {}
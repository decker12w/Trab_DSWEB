package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.trab_dsweb.enums.JobType;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateJobDTO(
        @NotBlank
        String description,

        @CNPJ
        @NotBlank
        String CNPJ,

        @NotNull
        JobType jobType,

        @NotNull
        UUID enterpriseId,

        @NotNull
        LocalDateTime applicationDeadline,

        @NotBlank
        String city,

        @NotNull
        List<String> skills,

        @NotNull
        Double remuneration
) {
}
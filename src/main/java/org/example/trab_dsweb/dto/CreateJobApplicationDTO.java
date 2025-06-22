package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateJobApplicationDTO(
        @NotNull
        UUID workerId,

        @NotNull
        UUID jobId
) {
}

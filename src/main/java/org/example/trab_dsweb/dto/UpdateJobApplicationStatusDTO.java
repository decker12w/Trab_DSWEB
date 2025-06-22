package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.NotNull;
import org.example.trab_dsweb.enums.Status;

public record UpdateJobApplicationStatusDTO(
        @NotNull
        Status status
) {}

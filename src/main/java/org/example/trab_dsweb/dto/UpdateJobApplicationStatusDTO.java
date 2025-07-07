package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.NotNull;
import org.example.trab_dsweb.indicator.Status;

public record UpdateJobApplicationStatusDTO(
        @NotNull
        Status status,

        String interviewLink
) {}

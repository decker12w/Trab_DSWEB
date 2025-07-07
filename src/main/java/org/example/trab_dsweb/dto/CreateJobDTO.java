package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.trab_dsweb.indicator.JobType;

import java.time.LocalDateTime;
import java.util.List;

public record CreateJobDTO(
        @NotBlank
        String title,

        @NotBlank
        String description,

        @NotNull
        JobType jobType,

        @NotNull
        LocalDateTime applicationDeadline,

        @NotBlank
        String city,

        @NotNull
        List<String> skills,

        @NotNull
        Double remuneration
) {}
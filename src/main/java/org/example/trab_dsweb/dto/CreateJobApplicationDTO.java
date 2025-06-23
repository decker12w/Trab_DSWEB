package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreateJobApplicationDTO(
        @NotNull
        MultipartFile curriculum,

        @NotNull
        UUID workerId,

        @NotNull
        UUID jobId
) {}

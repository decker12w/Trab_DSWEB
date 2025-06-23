package org.example.trab_dsweb.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreateJobApplicationDTO(
        @NotNull
        String workerEmail,

        @NotNull
        UUID jobId,

        @NotNull
        MultipartFile curriculum
) {
}

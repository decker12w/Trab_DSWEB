package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.enums.JobType;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateJobRequestDTO(
        String description,
        String CNPJ,
        JobType jobType,
        UUID enterpriseId,
        LocalDateTime applicationDeadline,
        String city
) {
}
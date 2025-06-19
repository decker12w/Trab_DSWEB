package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.enums.JobType;
import org.example.trab_dsweb.models.Job;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GetJobResponseDTO(
        UUID id,
        String description,
        String CNPJ,
        JobType jobType,
        EnterpriseInJobResponseDTO enterprise,
        LocalDateTime applicationDeadline,
        boolean jobActive,
        Double remuneration,
        List<String> skills,
        String city
) {
    public static GetJobResponseDTO mapJobToDTO(Job job) {
        EnterpriseInJobResponseDTO enterpriseDTO = new EnterpriseInJobResponseDTO(
                job.getEntreprise().getId(),
                job.getEntreprise().getName(),
                job.getEntreprise().getCnpj()
        );

        return new GetJobResponseDTO(
                job.getId(),
                job.getDescription(),
                job.getCNPJ(),
                job.getJobType(),
                enterpriseDTO,
                job.getApplicationDeadline(),
                job.isJobActive(),
                job.getRemuneration(),
                job.getSkills(),
                job.getCity());
    }
}

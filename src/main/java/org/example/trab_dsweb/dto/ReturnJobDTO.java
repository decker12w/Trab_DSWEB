package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.enums.JobType;
import org.example.trab_dsweb.models.Job;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReturnJobDTO(
        UUID id,
        String description,
        String CNPJ,
        JobType jobType,
        ReturnEnterpriseInJobDTO enterprise,
        LocalDateTime applicationDeadline,
        boolean jobActive,
        Double remuneration,
        List<String> skills,
        String city
) {
    public static ReturnJobDTO mapJobToDTO(Job job) {
        ReturnEnterpriseInJobDTO enterpriseDTO = new ReturnEnterpriseInJobDTO(
                job.getEntreprise().getId(),
                job.getEntreprise().getName(),
                job.getEntreprise().getCnpj()
        );

        return new ReturnJobDTO(
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

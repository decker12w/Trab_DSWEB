package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.enums.JobType;
import org.example.trab_dsweb.models.Job;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReturnJobDTO(
        UUID id,
        String title,
        String description,
        JobType jobType,
        LocalDateTime applicationDeadline,
        Double remuneration,
        List<String> skills,
        String city,
        boolean jobActive,
        ReturnEnterpriseInJobDTO enterprise
) {

    public String getSkillsAsString() {
        if (this.skills == null || this.skills.isEmpty()) {
            return "";
        }
        return String.join(", ", this.skills);
    }

    public static ReturnJobDTO mapJobToDTO(Job job) {
        ReturnEnterpriseInJobDTO enterpriseDTO = new ReturnEnterpriseInJobDTO(
                job.getEnterprise().getId(),
                job.getEnterprise().getName(),
                job.getEnterprise().getCnpj()
        );

        return new ReturnJobDTO(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getJobType(),
                job.getApplicationDeadline(),
                job.getRemuneration(),
                job.getSkills(),
                job.getCity(),
                job.isJobActive(),
                enterpriseDTO
        );
    }
}
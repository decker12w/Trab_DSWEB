package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.enums.Status;
import org.example.trab_dsweb.models.Job;
import org.example.trab_dsweb.models.JobApplication;
import org.example.trab_dsweb.models.Worker;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReturnJobApplicationDTO(
        UUID id,
        String description,
        LocalDateTime applicationDeadline,
        Status status,
        ReturnWorkerInJobDTO worker,
        ReturnEnterpriseInJobDTO enterprise
) {
    public static ReturnJobApplicationDTO mapJobApplicationToDTO(JobApplication jobApplication) {
        Worker worker = jobApplication.getWorker();
        ReturnWorkerInJobDTO workerDTO = new ReturnWorkerInJobDTO(
                worker.getId(),
                worker.getName(),
                worker.getCpf()
        );

        Job job = jobApplication.getJob();
        ReturnEnterpriseInJobDTO enterpriseDTO = new ReturnEnterpriseInJobDTO(
                job.getEnterprise().getId(),
                job.getEnterprise().getName(),
                job.getEnterprise().getCnpj()
        );

        return new ReturnJobApplicationDTO(
                jobApplication.getId(),
                job.getDescription(),
                job.getApplicationDeadline(),
                jobApplication.getStatus(),
                workerDTO,
                enterpriseDTO
        );
    }
}

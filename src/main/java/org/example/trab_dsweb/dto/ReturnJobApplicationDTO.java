package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.indicator.Status;
import org.example.trab_dsweb.model.Job;
import org.example.trab_dsweb.model.JobApplication;
import org.example.trab_dsweb.model.Worker;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReturnJobApplicationDTO(
        UUID id,
        String title,
        LocalDateTime applicationDeadline,
        Status status,
        ReturnWorkerInJobDTO worker,
        ReturnEnterpriseInJobDTO enterprise,
        byte[] curriculum
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
                job.getTitle(),
                job.getApplicationDeadline(),
                jobApplication.getStatus(),
                workerDTO,
                enterpriseDTO,
                jobApplication.getCurriculum()
        );
    }
}

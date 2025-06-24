package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dto.CreateJobDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.dto.ReturnWorkerInJobDTO;
import org.example.trab_dsweb.exceptions.exceptions.BadRequestException;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.models.Job;
import org.example.trab_dsweb.models.JobApplication;
import org.example.trab_dsweb.repositories.EnterpriseRepository;
import org.example.trab_dsweb.repositories.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class JobService {
    private JobRepository jobRepository;
    private EnterpriseRepository enterpriseRepository;

    public List<ReturnJobDTO> findAllActiveJobs() {
        return jobRepository.findAllByApplicationDeadlineAfter(LocalDateTime.now()).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    public List<ReturnJobDTO> findAllActiveJobsByCity(String city) {
        return jobRepository.findAllByApplicationDeadlineAfterAndCityContainingIgnoreCase(LocalDateTime.now(), city).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    public List<ReturnJobDTO> findAllJobsByEnterpriseId(UUID enterpriseId) {
        return jobRepository.findAllByEnterpriseId(enterpriseId).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    public void createJob(CreateJobDTO createJobRequestDTO, UUID enterpriseId) {
        Job job = new Job();
        job.setDescription(createJobRequestDTO.description());
        job.setTitle(createJobRequestDTO.title());
        job.setJobType(createJobRequestDTO.jobType());
        job.setApplicationDeadline(createJobRequestDTO.applicationDeadline());
        job.setSkills(createJobRequestDTO.skills());
        job.setRemuneration(createJobRequestDTO.remuneration());
        job.setCity(createJobRequestDTO.city());

        if (job.getApplicationDeadline().isBefore(LocalDateTime.now())) {
            log.error("Cannot create job: application deadline is in the past. Deadline={}, now={}",
                    createJobRequestDTO.applicationDeadline(), LocalDateTime.now());
            throw new BadRequestException("Application deadline must be a future date");
        }

        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> {
                    log.error("Enterprise not found with ID={}", enterpriseId);
                    return new NotFoundException("Enterprise not found");
                });
        job.setEnterprise(enterprise);

        jobRepository.save(job);
    }

    public ReturnJobDTO findById(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> {
                    log.error("Job not found with ID={}", jobId);
                    return new NotFoundException("Job not found with ID: " + jobId);
                });
        return ReturnJobDTO.mapJobToDTO(job);
    }
}

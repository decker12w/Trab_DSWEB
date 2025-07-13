package org.example.trab_dsweb.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dao.EnterpriseDAO;
import org.example.trab_dsweb.dao.JobDAO;
import org.example.trab_dsweb.dto.CreateJobDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.exception.exceptions.BadRequestException;
import org.example.trab_dsweb.exception.exceptions.NotFoundException;
import org.example.trab_dsweb.model.Enterprise;
import org.example.trab_dsweb.model.Job;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class JobService {
    private JobDAO jobDAO;
    private EnterpriseDAO enterpriseDAO;
    private final MessageSource messageSource;

    @Transactional(readOnly = true)
    public List<ReturnJobDTO> findAllJobs() {
        return StreamSupport.stream(jobDAO.findAll().spliterator(), false)
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReturnJobDTO> findAllActiveJobs() {
        return jobDAO.findAllByApplicationDeadlineAfter(LocalDateTime.now()).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReturnJobDTO> findAllActiveJobsByEnterpriseId(UUID enterpriseId) {
        return jobDAO.findAllByApplicationDeadlineAfterAndEnterpriseId(LocalDateTime.now(), enterpriseId).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReturnJobDTO> findAllActiveJobsByCity(String city) {
        return jobDAO.findAllByApplicationDeadlineAfterAndCityContainingIgnoreCase(LocalDateTime.now(), city).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReturnJobDTO> findAllJobsByEnterpriseId(UUID enterpriseId) {
        return jobDAO.findAllByEnterpriseId(enterpriseId).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
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
            throw new BadRequestException(messageSource.getMessage("error.job.badRequest.applicationDeadline", null, LocaleContextHolder.getLocale()));
        }

        Enterprise enterprise = enterpriseDAO.findById(enterpriseId)
                .orElseThrow(() -> {
                    log.error("Enterprise not found with ID={}", enterpriseId);
                    return new NotFoundException(messageSource.getMessage("error.enterprise.notfound", null, LocaleContextHolder.getLocale()));
                });
        job.setEnterprise(enterprise);

        jobDAO.save(job);
    }

    @Transactional(readOnly = true)
    public ReturnJobDTO findById(UUID jobId) {
        Job job = jobDAO.findById(jobId)
                .orElseThrow(() -> {
                    log.error("Job not found with ID={}", jobId);
                    return new NotFoundException(messageSource.getMessage("error.job.notfound", null, LocaleContextHolder.getLocale()));
                });
        return ReturnJobDTO.mapJobToDTO(job);
    }
}

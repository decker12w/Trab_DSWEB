package org.example.trab_dsweb.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dao.JobApplicationDAO;
import org.example.trab_dsweb.dao.JobDAO;
import org.example.trab_dsweb.dao.WorkerDAO;
import org.example.trab_dsweb.dto.CreateJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.UpdateJobApplicationStatusDTO;
import org.example.trab_dsweb.indicator.Status;
import org.example.trab_dsweb.exception.exceptions.BadRequestException;
import org.example.trab_dsweb.exception.exceptions.ConflictException;
import org.example.trab_dsweb.exception.exceptions.NotFoundException;
import org.example.trab_dsweb.model.Job;
import org.example.trab_dsweb.model.JobApplication;
import org.example.trab_dsweb.model.Worker;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class JobApplicationService {
    private JobApplicationDAO jobApplicationDAO;
    private JobDAO jobDAO;
    private WorkerDAO workerDAO;
    private EmailService emailService;
    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Transactional
    public List<ReturnJobApplicationDTO> findAllJobApplicationsByWorkerId(UUID id) {
        return jobApplicationDAO.findAllByWorkerId(id).stream()
                .map(ReturnJobApplicationDTO::mapJobApplicationToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createJobApplication(CreateJobApplicationDTO createJobApplicationRequestDTO) {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setStatus(Status.OPEN);

        Worker worker = workerDAO.findById(createJobApplicationRequestDTO.workerId())
                .orElseThrow(() -> {
                    log.error("Worker not found with ID={}", createJobApplicationRequestDTO.workerId());
                    return new NotFoundException(messageSource.getMessage("error.worker.notfound", null, locale));
                });
        jobApplication.setWorker(worker);

        Job job = jobDAO.findById(createJobApplicationRequestDTO.jobId())
                .orElseThrow(() -> {
                    log.error("Job not found with ID={}", createJobApplicationRequestDTO.jobId());
                    return new NotFoundException(messageSource.getMessage("error.job.notfound", null, locale));
                });
        jobApplication.setJob(job);

        if (job.getApplicationDeadline().isBefore(LocalDateTime.now())) {
            log.error("Application deadline has expired for job ID={}. Deadline={}, now={}",
                    job.getId(), job.getApplicationDeadline(), LocalDateTime.now());
            throw new BadRequestException(messageSource.getMessage("error.jobApplication.badRequest.applicationDeadline", null, locale));
        }

        if (jobApplicationDAO.findByWorkerIdAndJobId(worker.getId(), job.getId()).isPresent()) {
            log.error("Worker ID={} already applied for job ID={}", worker.getId(), job.getId());
            throw new ConflictException(messageSource.getMessage("error.jobApplication.conflict.applied", null, locale));
        }

        try {
            jobApplication.setCurriculum(createJobApplicationRequestDTO.curriculum().getBytes());
        } catch (IOException e) {
            log.error("Failed to process curriculum file: {}", e.getMessage(), e);
            throw new BadRequestException(messageSource.getMessage("error.jobApplication.badRequest.curriculum", null, locale));
        }

        jobApplicationDAO.save(jobApplication);
    }

    public void updateJobApplicationStatus(UUID id, UpdateJobApplicationStatusDTO updateJobApplicationStatusRequestDTO) {
        JobApplication jobApplication = jobApplicationDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("JobApplication not found with ID={}", id);
                    return new NotFoundException(messageSource.getMessage("error.jobApplication.notfound", null, locale));
                });

        jobApplication.setStatus(updateJobApplicationStatusRequestDTO.status());
        if (jobApplication.getStatus() == Status.INTERVIEW) {
            emailService.sendEmail(
                    jobApplication.getWorker().getEmail(),
                    "Interview " + jobApplication.getJob().getDescription(),
                    "Link: " + updateJobApplicationStatusRequestDTO.interviewLink()
            );
        }

        jobApplicationDAO.save(jobApplication);
    }

    public void deleteJobApplicationById(UUID id) {
        if (!jobApplicationDAO.existsById(id)) {
            log.error("Attempt to delete non-existing Job application with ID={}", id);
            throw new NotFoundException(messageSource.getMessage("error.jobApplication.notfound", null, locale));
        }
        jobApplicationDAO.deleteById(id);
    }

    public ReturnJobApplicationDTO getJobApplicationById(UUID id) {
        JobApplication jobApplication = jobApplicationDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("JobApplication not found with ID={}", id);
                    return new NotFoundException(messageSource.getMessage("error.jobApplication.notfound", null, locale));
                });

        return  ReturnJobApplicationDTO.mapJobApplicationToDTO(jobApplication);
    }

    @Transactional
    public List<ReturnJobApplicationDTO> findByJobId(UUID jobId) {
        if (!jobDAO.existsById(jobId)) {
            log.error("Attempt to fetch applications for a non-existing job with ID={}", jobId);
            throw new NotFoundException(messageSource.getMessage("error.job.notfound", null, locale));
        }
        return jobApplicationDAO.findByJobId(jobId).stream()
                .map(ReturnJobApplicationDTO::mapJobApplicationToDTO)
                .collect(Collectors.toList());
    }
}

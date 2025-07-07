package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.daos.JobApplicationDAO;
import org.example.trab_dsweb.daos.JobDAO;
import org.example.trab_dsweb.daos.WorkerDAO;
import org.example.trab_dsweb.dto.CreateJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.UpdateJobApplicationStatusDTO;
import org.example.trab_dsweb.enums.Status;
import org.example.trab_dsweb.exceptions.exceptions.BadRequestException;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Job;
import org.example.trab_dsweb.models.JobApplication;
import org.example.trab_dsweb.models.Worker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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
                    log.error("Worker not found with email={}", createJobApplicationRequestDTO.workerId());
                    return new NotFoundException("Worker not found with email: " + createJobApplicationRequestDTO.workerId());
                });
        jobApplication.setWorker(worker);

        Job job = jobDAO.findById(createJobApplicationRequestDTO.jobId())
                .orElseThrow(() -> {
                    log.error("Job not found with ID={}", createJobApplicationRequestDTO.jobId());
                    return new NotFoundException("Job not found with ID: " + createJobApplicationRequestDTO.jobId());
                });
        jobApplication.setJob(job);

        if (job.getApplicationDeadline().isBefore(LocalDateTime.now())) {
            log.error("Application deadline has expired for job ID={}. Deadline={}, now={}",
                    job.getId(), job.getApplicationDeadline(), LocalDateTime.now());
            throw new BadRequestException("Applications for this job are closed due to the expired deadline");
        }

        if (jobApplicationDAO.findByWorkerIdAndJobId(worker.getId(), job.getId()).isPresent()) {
            log.error("Worker ID={} has already applied for job ID={}", worker.getId(), job.getId());
            throw new ConflictException("Worker have already applied for this vacancy");
        }

        try {
            jobApplication.setCurriculum(createJobApplicationRequestDTO.curriculum().getBytes());
        } catch (IOException e) {
            log.error("Failed to process curriculum file: {}", e.getMessage(), e);
            throw new BadRequestException("Error uploading curriculum");
        }

        jobApplicationDAO.save(jobApplication);
    }

    public void updateJobApplicationStatus(UUID id, UpdateJobApplicationStatusDTO updateJobApplicationStatusRequestDTO) {
        JobApplication jobApplication = jobApplicationDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("JobApplication not found with ID={}", id);
                    return new NotFoundException("Job application not found with ID: " + id);
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
            throw new NotFoundException("Job application not found");
        }
        jobApplicationDAO.deleteById(id);
    }

    public ReturnJobApplicationDTO getJobApplicationById(UUID id) {
        JobApplication jobApplication = jobApplicationDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("JobApplication not found with ID={}", id);
                    return new NotFoundException("Job application not found with ID: " + id);
                });

        return  ReturnJobApplicationDTO.mapJobApplicationToDTO(jobApplication);
    }

    @Transactional
    public List<ReturnJobApplicationDTO> findByJobId(UUID jobId) {
        if (!jobDAO.existsById(jobId)) {
            log.error("Attempt to fetch applications for a non-existing job with ID={}", jobId);
            throw new NotFoundException("Job not found with ID: " + jobId);
        }
        return jobApplicationDAO.findByJobId(jobId).stream()
                .map(ReturnJobApplicationDTO::mapJobApplicationToDTO)
                .collect(Collectors.toList());
    }
}

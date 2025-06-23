package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.example.trab_dsweb.repositories.JobApplicationRepository;
import org.example.trab_dsweb.repositories.JobRepository;
import org.example.trab_dsweb.repositories.WorkerRepository;
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
    private JobApplicationRepository jobApplicationRepository;
    private JobRepository jobRepository;
    private WorkerRepository workerRepository;
    private EmailService emailService;

    public void createJobApplication(CreateJobApplicationDTO createJobApplicationRequestDTO) {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setStatus(Status.OPEN);

        Worker worker = workerRepository.findWorkerByEmail(createJobApplicationRequestDTO.workerEmail())
                .orElseThrow(() -> {
                    log.error("Worker not found with email={}", createJobApplicationRequestDTO.workerEmail());
                    return new NotFoundException("Worker not found with email: " + createJobApplicationRequestDTO.workerEmail());
                });
        jobApplication.setWorker(worker);

        Job job = jobRepository.findById(createJobApplicationRequestDTO.jobId())
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

        if (jobApplicationRepository.findByWorkerIdAndJobId(worker.getId(), job.getId()).isPresent()) {
            log.error("Worker ID={} has already applied for job ID={}", worker.getId(), job.getId());
            throw new ConflictException("Worker have already applied for this vacancy");
        }

        try {
            jobApplication.setCurriculum(createJobApplicationRequestDTO.curriculum().getBytes());
        } catch (IOException e) {
            log.error("Failed to process curriculum file: {}", e.getMessage(), e);
            throw new BadRequestException("Error uploading curriculum");
        }

        jobApplicationRepository.save(jobApplication);
    }

    public void updateJobApplicationStatus(UUID id, UpdateJobApplicationStatusDTO updateJobApplicationStatusRequestDTO) {
        JobApplication jobApplication = jobApplicationRepository.findById(id)
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

        jobApplicationRepository.save(jobApplication);
    }

    @Transactional(readOnly = true)
    public List<ReturnJobApplicationDTO> findAllJobApplicationsByWorkerEmail(String email) {
        return jobApplicationRepository.findAllByWorkerEmail(email).stream()
                .map(ReturnJobApplicationDTO::mapJobApplicationToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReturnJobApplicationDTO> findAllJobApplicationsByJobId(UUID jobId) {
        return jobApplicationRepository.findAllByJobId(jobId).stream()
                .map(ReturnJobApplicationDTO::mapJobApplicationToDTO)
                .collect(Collectors.toList());
    }
}

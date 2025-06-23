package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.UpdateJobApplicationStatusDTO;
import org.example.trab_dsweb.enums.Status;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobApplicationService {
    private JobApplicationRepository jobApplicationRepository;
    private JobRepository jobRepository;
    private WorkerRepository workerRepository;

    public ReturnJobApplicationDTO createJobApplication(CreateJobApplicationDTO createJobApplicationRequestDTO) {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setStatus(Status.OPEN);

        Worker worker = workerRepository.findById(createJobApplicationRequestDTO.workerId())
                .orElseThrow(() -> new NotFoundException("Worker not found with ID: " + createJobApplicationRequestDTO.workerId()));
        jobApplication.setWorker(worker);

        Job job = jobRepository.findById(createJobApplicationRequestDTO.jobId())
                .orElseThrow(() -> new NotFoundException("Job not found with ID: " + createJobApplicationRequestDTO.jobId()));
        jobApplication.setJob(job);

        if (jobApplicationRepository.findByWorkerIdAndJobId(worker.getId(), job.getId()).isPresent()) {
            throw new ConflictException("Worker have already applied for this vacancy");
        }

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        return ReturnJobApplicationDTO.mapJobApplicationToDTO(savedJobApplication);
    }

    public ReturnJobApplicationDTO updateJobApplicationStatus(UUID id, UpdateJobApplicationStatusDTO updateJobApplicationStatusRequestDTO) {
        JobApplication jobApplication = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job application not found with ID: " + id));
        jobApplication.setStatus(updateJobApplicationStatusRequestDTO.status());

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        return ReturnJobApplicationDTO.mapJobApplicationToDTO(savedJobApplication);
    }

    @Transactional(readOnly = true)
    public List<ReturnJobApplicationDTO> findAllJobApplicationsByWorkerId(UUID workerId) {
        return jobApplicationRepository.findAllByWorkerId(workerId).stream()
                .map(ReturnJobApplicationDTO::mapJobApplicationToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReturnJobApplicationDTO> findAllJobApplicationsByJobId(UUID jobId) {
        return jobApplicationRepository.findAllByJobId(jobId).stream()
                .map(ReturnJobApplicationDTO::mapJobApplicationToDTO)
                .collect(Collectors.toList());
    }
<<<<<<< HEAD

    @Transactional(readOnly = true)
    public List<ReturnJobApplicationDTO> listWorkersByJob(UUID vagaId) {
        return jobApplicationRepository.findAllByJobId(vagaId)
                .stream()
                .map(ReturnJobApplicationDTO::mapJobApplicationToDTO)
                .collect(Collectors.toList());
    }
=======
>>>>>>> 477f962c9b4c7d2fab8a7e773aa260168c2cf2da
}

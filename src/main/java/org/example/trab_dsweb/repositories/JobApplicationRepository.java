package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {
    List<JobApplication> findAllByWorkerId(UUID workerId);
    List<JobApplication> findAllByJobId(UUID jobId);
    Optional<JobApplication> findByWorkerIdAndJobId(UUID workerId, UUID jobId);
}

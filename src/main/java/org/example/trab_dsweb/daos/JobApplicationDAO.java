package org.example.trab_dsweb.daos;

import org.example.trab_dsweb.models.JobApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobApplicationDAO extends CrudRepository<JobApplication, UUID> {
    List<JobApplication> findAllByWorkerId(UUID workerId);
    Optional<JobApplication> findByWorkerIdAndJobId(UUID workerId, UUID jobId);
    List<JobApplication> findByJobId(UUID jobId);
}

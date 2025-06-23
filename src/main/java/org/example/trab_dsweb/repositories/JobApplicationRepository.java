package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {
    List<JobApplication> findAllByWorkerId(UUID workerId);
<<<<<<< HEAD
    List<JobApplication> findAllByJobId(UUID workerId);


=======
    List<JobApplication> findAllByJobId(UUID jobId);
>>>>>>> 477f962c9b4c7d2fab8a7e773aa260168c2cf2da
    Optional<JobApplication> findByWorkerIdAndJobId(UUID workerId, UUID jobId);
}

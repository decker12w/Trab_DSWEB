package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.Job;
import org.example.trab_dsweb.models.JobApplication;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends CrudRepository<Job, UUID> {
    List<Job> findAllByApplicationDeadlineAfter(LocalDateTime now);
    List<Job> findAllByApplicationDeadlineAfterAndCityContainingIgnoreCase(LocalDateTime now, String city);
    List<Job> findAllByEnterpriseId(UUID enterpriseId);
}

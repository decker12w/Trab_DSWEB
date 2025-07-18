package org.example.trab_dsweb.dao;

import org.example.trab_dsweb.model.Job;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JobDAO extends CrudRepository<Job, UUID> {
    List<Job> findAllByApplicationDeadlineAfter(LocalDateTime now);
    List<Job> findAllByApplicationDeadlineAfterAndEnterpriseId(LocalDateTime now,  UUID enterpriseId);
    List<Job> findAllByApplicationDeadlineAfterAndCityContainingIgnoreCase(LocalDateTime now, String city);
    List<Job> findAllByEnterpriseId(UUID enterpriseId);
}

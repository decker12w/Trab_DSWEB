package org.example.trab_dsweb.daos;

import org.example.trab_dsweb.models.Job;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JobDAO extends CrudRepository<Job, UUID> {
    List<Job> findAllByApplicationDeadlineAfter(LocalDateTime now);
    List<Job> findAllByApplicationDeadlineAfterAndCityContainingIgnoreCase(LocalDateTime now, String city);
    List<Job> findAllByEnterpriseId(UUID enterpriseId);
}

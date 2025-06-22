package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.Job;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends CrudRepository<Job, UUID> {
    List<Job> findByJobActiveTrueAndCity(String city);
    List<Job> findAllByEnterpriseId(UUID enterpriseId);
    List<Job> findAllActiveTrue();
}

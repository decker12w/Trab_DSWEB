package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.Worker;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkerRepository extends CrudRepository<Worker, UUID> {
    Optional<Worker> findWorkerByCpf(String cpf);
    Optional<Worker> findWorkerByEmail(String email);

    @Override
    List<Worker> findAll();
}

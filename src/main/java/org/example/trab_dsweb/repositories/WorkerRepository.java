package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.Worker;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkerRepository extends CrudRepository<Worker, UUID> {
    Optional<Worker> findByCpf(String cpf);
    Optional<Worker> findByEmail(String email);
}

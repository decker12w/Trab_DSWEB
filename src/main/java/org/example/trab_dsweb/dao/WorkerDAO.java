package org.example.trab_dsweb.dao;

import org.example.trab_dsweb.model.Worker;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkerDAO extends CrudRepository<Worker, UUID> {
    Optional<Worker> findByCpf(String cpf);
    Optional<Worker> findByEmail(String email);
}

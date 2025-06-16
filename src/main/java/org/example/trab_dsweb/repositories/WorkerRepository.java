package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkerRepository extends JpaRepository<Worker, UUID> {
    Optional<Worker> findWorkerByCpf(String cpf);
    Optional<Worker> findWorkerByEmail(String email);


}

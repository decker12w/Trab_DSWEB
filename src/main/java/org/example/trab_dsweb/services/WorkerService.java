package org.example.trab_dsweb.services;

import org.example.trab_dsweb.dto.CreateWorkerRequestDTO;
import org.example.trab_dsweb.dto.CreateWorkerResponseDTO;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.repositories.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public CreateWorkerResponseDTO create(CreateWorkerRequestDTO data) {
        workerRepository.findWorkerByCpf(data.cpf())
                .orElseThrow(() -> new IllegalArgumentException("Worker with this CPF already exists"));

        workerRepository.findWorkerByEmail(data.email())
                .orElseThrow(() -> new IllegalArgumentException("Worker with this email already exists"));

        Worker newWorker = new Worker(
                data.email(),
                data.password(),
                data.cpf(),
                data.name(),
                data.gender(),
                data.birthDate()
        );

        Worker savedWorker = workerRepository.save(newWorker);

        return new CreateWorkerResponseDTO(
                savedWorker.getId(),
                savedWorker.getEmail(),
                savedWorker.getCpf(),
                savedWorker.getName(),
                savedWorker.getGender(),
                savedWorker.getBirthDate()
        );
    }


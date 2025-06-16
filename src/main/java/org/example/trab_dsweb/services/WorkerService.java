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

    public CreateWorkerResponseDTO create(CreateWorkerRequestDTO worker) {
        OptionalworkerRepository.findWorkerByCpf(worker.cpf()).orElseThrow(new IllegalCallerException(
                "Já existe um trabalhador cadastrado com o CPF: " + worker.cpf()
        ));
    }


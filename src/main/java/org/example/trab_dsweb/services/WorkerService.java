package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateWorkerRequestDTO;
import org.example.trab_dsweb.dto.CreateWorkerResponseDTO;
import org.example.trab_dsweb.dto.GetWorkerResponseDTO;
import org.example.trab_dsweb.dto.UpdateWorkerRequestDTO;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.repositories.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;

    public CreateWorkerResponseDTO createWorker(CreateWorkerRequestDTO data) {
        if (workerRepository.findWorkerByCpf(data.cpf()).isPresent()) {
            throw new ConflictException("Worker with this CPF already exists");
        }
    public WorkerService(WorkerRepository workerRepository) {
            this.workerRepository = workerRepository;
        }

        if (workerRepository.findWorkerByEmail(data.email()).isPresent()) {
            throw new ConflictException("Worker with this email already exists");
        }
        public CreateWorkerResponseDTO create(CreateWorkerRequestDTO data) {
            workerRepository.findWorkerByCpf(data.cpf())
                    .orElseThrow(() -> new IllegalArgumentException("Worker with this CPF already exists"));

            Worker newWorker = new Worker();
            newWorker.setEmail(data.email());
            newWorker.setPassword(data.password());
            newWorker.setCpf(data.cpf());
            newWorker.setName(data.name());
            newWorker.setGender(data.gender());
            newWorker.setBirthDate(data.birthDate());
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

            @@ -47,67 +42,4 @@ public CreateWorkerResponseDTO createWorker(CreateWorkerRequestDTO data) {
                savedWorker.getBirthDate()
        );
            }

            public GetWorkerResponseDTO getWorkerById(UUID id) {
                Worker worker = workerRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Worker not found"));

                return new GetWorkerResponseDTO(
                        worker.getId(),
                        worker.getEmail(),
                        worker.getCpf(),
                        worker.getName(),
                        worker.getGender(),
                        worker.getBirthDate()
                );
            }

            public void deleteWorkerById(UUID id) {
                if (!workerRepository.existsById(id)) {
                    throw new NotFoundException("Worker not found");
                }
                workerRepository.deleteById(id);
            }

            public GetWorkerResponseDTO updateWorkerById(UUID id, UpdateWorkerRequestDTO data) {
                Worker existingWorker = workerRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Worker not found"));

                if (data.cpf() != null && !data.cpf().isEmpty()) {
                    workerRepository.findWorkerByCpf(data.cpf()).ifPresent(worker -> {
                        if (!worker.getId().equals(id)) {
                            throw new ConflictException("Worker with this CPF already exists");
                        }
                    });
                    existingWorker.setCpf(data.cpf());
                }

                if (data.email() != null && !data.email().isEmpty()) {
                    workerRepository.findWorkerByEmail(data.email()).ifPresent(worker -> {
                        if (!worker.getId().equals(id)) {
                            throw new ConflictException("Worker with this email already exists");
                        }
                    });
                    existingWorker.setEmail(data.email());
                }

                if (data.password() != null && !data.password().isEmpty()) {
                    existingWorker.setPassword(data.password());
                }

                if (data.name() != null && !data.name().isEmpty()) {
                    existingWorker.setName(data.name());
                }

                Worker updatedWorker = workerRepository.save(existingWorker);

                return new GetWorkerResponseDTO(
                        updatedWorker.getId(),
                        updatedWorker.getEmail(),
                        updatedWorker.getCpf(),
                        updatedWorker.getName(),
                        updatedWorker.getGender(),
                        updatedWorker.getBirthDate()
                );
            }
        }
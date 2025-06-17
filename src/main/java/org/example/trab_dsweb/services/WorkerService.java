package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateWorkerRequestDTO;
import org.example.trab_dsweb.dto.CreateWorkerResponseDTO;
import org.example.trab_dsweb.dto.GetWorkerResponseDTO;
import org.example.trab_dsweb.dto.UpdateWorkerRequestDTO;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.repositories.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;

    public CreateWorkerResponseDTO createWorker(CreateWorkerRequestDTO data) {

        Optional<Worker> existingWorkerByCpf = workerRepository.findWorkerByCpf(data.cpf());
        if (existingWorkerByCpf.isPresent()) {
            throw new IllegalArgumentException("Worker with this CPF already exists");
        }

        Optional<Worker> existingWorkerByEmail = workerRepository.findWorkerByEmail(data.email());
        if (existingWorkerByEmail.isPresent()) {
            throw new IllegalArgumentException("Worker with this email already exists");
        }

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

    public GetWorkerResponseDTO getWorkerById(UUID id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

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
            throw new IllegalArgumentException("Worker not found");
        }
        workerRepository.deleteById(id);
    }

    public GetWorkerResponseDTO updateWorkerById(UUID id, UpdateWorkerRequestDTO data) {
        Worker existingWorker = workerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        // Only update fields if they are provided in the DTO
        if (data.email() != null && !data.email().isEmpty()) {
            existingWorker.setEmail(data.email());
        }
        if (data.password() != null && !data.password().isEmpty()) {
            existingWorker.setPassword(data.password());
        }
        if (data.cpf() != null && !data.cpf().isEmpty()) {
            existingWorker.setCpf(data.cpf());
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
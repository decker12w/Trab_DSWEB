package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.repositories.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;

    public ReturnWorkerDTO createWorker(CreateWorkerDTO data) {
        if (workerRepository.findWorkerByCpf(data.cpf()).isPresent()) {
            throw new ConflictException("Worker with this CPF already exists");
        }

        if (workerRepository.findWorkerByEmail(data.email()).isPresent()) {
            throw new ConflictException("Worker with this email already exists");
        }

        Worker newWorker = new Worker();
        newWorker.setEmail(data.email());
        newWorker.setPassword(data.password());
        newWorker.setCpf(data.cpf());
        newWorker.setName(data.name());
        newWorker.setGender(data.gender());
        newWorker.setBirthDate(data.birthDate());

        Worker savedWorker = workerRepository.save(newWorker);

        return new ReturnWorkerDTO(
                savedWorker.getId(),
                savedWorker.getEmail(),
                savedWorker.getCpf(),
                savedWorker.getName(),
                savedWorker.getGender(),
                savedWorker.getBirthDate()
        );
    }

    public ReturnWorkerDTO getWorkerById(UUID id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Worker not found"));

        return new ReturnWorkerDTO(
                worker.getId(),
                worker.getEmail(),
                worker.getCpf(),
                worker.getName(),
                worker.getGender(),
                worker.getBirthDate()
        );
    }
    public List<ReturnWorkerDTO> listAllWorkers() {
        return workerRepository.findAll().stream()
                .map(worker -> new ReturnWorkerDTO(
                        worker.getId(),
                        worker.getEmail(),
                        worker.getCpf(),
                        worker.getName(),
                        worker.getGender(),
                        worker.getBirthDate()))
                .toList();
    }

    public void deleteWorkerById(UUID id) {
        if (!workerRepository.existsById(id)) {
            throw new NotFoundException("Worker not found");
        }
        workerRepository.deleteById(id);
    }

    public ReturnWorkerDTO updateWorkerById(UUID id, CreateWorkerDTO data) {
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

        return new ReturnWorkerDTO(
                updatedWorker.getId(),
                updatedWorker.getEmail(),
                updatedWorker.getCpf(),
                updatedWorker.getName(),
                updatedWorker.getGender(),
                updatedWorker.getBirthDate()
        );
    }
}
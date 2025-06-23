package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.repositories.WorkerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class WorkerService {
    private BCryptPasswordEncoder encoder;
    private final WorkerRepository workerRepository;

    public void createWorker(CreateWorkerDTO data) {
        if (workerRepository.findWorkerByCpf(data.cpf()).isPresent()) {
            log.error("Worker with CPF={} already exists", data.cpf());
            throw new ConflictException("Profissional com esse CPF já existe");
        }

        if (workerRepository.findWorkerByEmail(data.email()).isPresent()) {
            log.error("Worker with email={} already exists", data.email());
            throw new ConflictException("Profissional com esse e-mail já existe");
        }

        Worker newWorker = new Worker();
        newWorker.setEmail(data.email());
        newWorker.setPassword(encoder.encode(data.password()));
        newWorker.setCpf(data.cpf());
        newWorker.setName(data.name());
        newWorker.setGender(data.gender());
        newWorker.setBirthDate(data.birthDate());

        workerRepository.save(newWorker);
    }

    public ReturnWorkerDTO getWorkerById(UUID id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Worker not found with ID={}", id);
                    return new NotFoundException("Worker not found");
                });

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
            log.error("Attempt to delete non-existing Worker with ID={}", id);
            throw new NotFoundException("Worker not found");
        }
        workerRepository.deleteById(id);
    }

    public ReturnWorkerDTO updateWorkerById(UUID id, CreateWorkerDTO data) {
        Worker existingWorker = workerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Worker not found with ID={}", id);
                    return new NotFoundException("Worker not found");
                });

        if (data.cpf() != null && !data.cpf().isEmpty()) {
            workerRepository.findWorkerByCpf(data.cpf()).ifPresent(worker -> {
                if (!worker.getId().equals(id)) {
                    log.error("Duplicate CPF={} on update for Worker ID={}", data.cpf(), id);
                    throw new ConflictException("Worker with this CPF already exists");
                }
            });
            existingWorker.setCpf(data.cpf());
        }

        if (data.email() != null && !data.email().isEmpty()) {
            workerRepository.findWorkerByEmail(data.email()).ifPresent(worker -> {
                if (!worker.getId().equals(id)) {
                    log.error("Duplicate email={} on update for Worker ID={}", data.email(), id);
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
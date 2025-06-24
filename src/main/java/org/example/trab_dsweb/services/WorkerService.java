package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.daos.WorkerDAO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class WorkerService {
    private BCryptPasswordEncoder encoder;
    private final WorkerDAO workerDAO;

    public ReturnWorkerDTO findWorkerById(UUID id) {
        Worker worker = workerDAO.findById(id)
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
        return StreamSupport.stream(workerDAO.findAll().spliterator(), false)
                .map(worker -> new ReturnWorkerDTO(
                        worker.getId(),
                        worker.getEmail(),
                        worker.getCpf(),
                        worker.getName(),
                        worker.getGender(),
                        worker.getBirthDate()))
                .toList();
    }

    public void createWorker(CreateWorkerDTO data) {
        if (workerDAO.findByCpf(data.cpf()).isPresent()) {
            log.error("Worker with CPF={} already exists", data.cpf());
            throw new ConflictException("Profissional com esse CPF já existe");
        }

        if (workerDAO.findByEmail(data.email()).isPresent()) {
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

        workerDAO.save(newWorker);
    }

    public void updateWorkerById(UUID id, CreateWorkerDTO data) {
        Worker existingWorker = workerDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("Worker not found with ID={}", id);
                    return new NotFoundException("Worker not found");
                });

        if (data.cpf() != null && !data.cpf().isEmpty()) {
            workerDAO.findByCpf(data.cpf()).ifPresent(worker -> {
                if (!worker.getId().equals(id)) {
                    log.error("Duplicate CPF={} on update for Worker ID={}", data.cpf(), id);
                    throw new ConflictException("Worker with this CPF already exists");
                }
            });
            existingWorker.setCpf(data.cpf());
        }

        if (data.email() != null && !data.email().isEmpty()) {
            workerDAO.findByEmail(data.email()).ifPresent(worker -> {
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

        workerDAO.save(existingWorker);
    }

    public void deleteWorkerById(UUID id) {
        if (!workerDAO.existsById(id)) {
            log.error("Attempt to delete non-existing Worker with ID={}", id);
            throw new NotFoundException("Worker not found");
        }
        workerDAO.deleteById(id);
    }
}
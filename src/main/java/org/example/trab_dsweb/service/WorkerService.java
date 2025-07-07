package org.example.trab_dsweb.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dao.JobApplicationDAO;
import org.example.trab_dsweb.dao.WorkerDAO;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.exception.exceptions.BadRequestException;
import org.example.trab_dsweb.exception.exceptions.ConflictException;
import org.example.trab_dsweb.exception.exceptions.NotFoundException;
import org.example.trab_dsweb.model.Worker;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class WorkerService {
    private final JobApplicationDAO jobApplicationDAO;
    private BCryptPasswordEncoder encoder;
    private final WorkerDAO workerDAO;
    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    public ReturnWorkerDTO findWorkerById(UUID id) {
        Worker worker = workerDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("Worker not found with ID={}", id);
                    return new NotFoundException(messageSource.getMessage("error.worker.notfound", null, locale));
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
            throw new ConflictException(messageSource.getMessage("error.worker.conflict.cpf", null, locale));
        }

        if (workerDAO.findByEmail(data.email()).isPresent()) {
            log.error("Worker with email={} already exists", data.email());
            throw new ConflictException(messageSource.getMessage("error.worker.conflict.email", null, locale));
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
                    return new NotFoundException(messageSource.getMessage("error.worker.notfound", null, locale));
                });

        if (data.cpf() != null && !data.cpf().isEmpty()) {
            workerDAO.findByCpf(data.cpf()).ifPresent(worker -> {
                if (!worker.getId().equals(id)) {
                    log.error("Duplicate CPF={} on update for Worker ID={}", data.cpf(), id);
                    throw new ConflictException(messageSource.getMessage("error.worker.conflict.cpf", null, locale));
                }
            });
            existingWorker.setCpf(data.cpf());
        }

        if (data.email() != null && !data.email().isEmpty()) {
            workerDAO.findByEmail(data.email()).ifPresent(worker -> {
                if (!worker.getId().equals(id)) {
                    log.error("Duplicate email={} on update for Worker ID={}", data.email(), id);
                    throw new ConflictException(messageSource.getMessage("error.worker.conflict.email", null, locale));
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

        if (data.birthDate() != null) {
            existingWorker.setBirthDate(data.birthDate());
        }

        if (data.gender() != null) {
            existingWorker.setGender(data.gender());
        }

        workerDAO.save(existingWorker);
    }

    public void deleteWorkerById(UUID id) {
        if (!workerDAO.existsById(id)) {
            log.error("Attempt to delete non-existing Worker with ID={}", id);
            throw new NotFoundException(messageSource.getMessage("error.worker.notfound", null, locale));
        }
        if (!jobApplicationDAO.findAllByWorkerId(id).isEmpty()) {
            log.error("Cannot delete Worker with ID={} because it has job applications", id);
            throw new BadRequestException(messageSource.getMessage("error.worker.delete.conflict", null, locale));
        }
        workerDAO.deleteById(id);
    }
}
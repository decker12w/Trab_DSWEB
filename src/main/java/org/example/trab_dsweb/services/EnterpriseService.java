package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dao.EnterpriseDAO;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Enterprise;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class EnterpriseService {
    private BCryptPasswordEncoder encoder;
    private final EnterpriseDAO enterpriseDAO;

    public List<ReturnEnterpriseDTO> findAllEnterprises() {
        return StreamSupport.stream(enterpriseDAO.findAll().spliterator(), false)
                .map(enterprise -> new ReturnEnterpriseDTO(
                        enterprise.getId(),
                        enterprise.getEmail(),
                        enterprise.getCnpj(),
                        enterprise.getName(),
                        enterprise.getDescription(),
                        enterprise.getCity()
                ))
                .collect(Collectors.toList());
    }

    public ReturnEnterpriseDTO findEnterpriseById(UUID id) {
        Enterprise enterprise = enterpriseDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("Enterprise not found with ID={}", id);
                    return new NotFoundException("Enterprise not found");
                });

        return new ReturnEnterpriseDTO(
                enterprise.getId(),
                enterprise.getEmail(),
                enterprise.getCnpj(),
                enterprise.getName(),
                enterprise.getDescription(),
                enterprise.getCity()
        );
    }

    public void createEnterprise(CreateEnterpriseDTO data){
        enterpriseDAO.findByEmail(data.email())
                .ifPresent(enterprise -> {
                    log.error("Enterprise with email={} already exists", data.email());
                    throw new ConflictException("Enterprise with this email already exists");
                });

        enterpriseDAO.findByCnpj(data.cnpj())
                .ifPresent(enterprise -> {
                    log.error("Enterprise with CNPJ={} already exists", data.cnpj());
                    throw new ConflictException("Enterprise with this CNPJ already exists");
                });

        Enterprise newEnterprise = new Enterprise();
        newEnterprise.setName(data.name());
        newEnterprise.setEmail(data.email());
        newEnterprise.setCnpj(data.cnpj());
        newEnterprise.setPassword(encoder.encode(data.password()));
        newEnterprise.setDescription(data.description());
        newEnterprise.setCity(data.city());

        enterpriseDAO.save(newEnterprise);
    }

    public void updateEnterpriseById(UUID id, CreateEnterpriseDTO data) {
        Enterprise existingEnterprise = enterpriseDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("Enterprise not found with ID={}", id);
                    return new NotFoundException("Enterprise not found");
                });

        if (data.cnpj() != null && !data.cnpj().isEmpty()) {
            enterpriseDAO.findByCnpj(data.cnpj()).ifPresent(ent -> {
                if (!ent.getId().equals(id)) {
                    log.error("Duplicate CNPJ={} on update for Enterprise ID={}", data.cnpj(), id);
                    throw new ConflictException("Enterprise with this CNPJ already exists");
                }
            });
            existingEnterprise.setCnpj(data.cnpj());
        }

        if (data.email() != null && !data.email().isEmpty()) {
            enterpriseDAO.findByEmail(data.email()).ifPresent(ent -> {
                if (!ent.getId().equals(id)) {
                    log.error("Duplicate email={} on update for Enterprise ID={}", data.email(), id);
                    throw new ConflictException("Enterprise with this email already exists");
                }
            });
            existingEnterprise.setEmail(data.email());
        }

        if (data.password() != null && !data.password().isEmpty()) {
            existingEnterprise.setPassword(data.password());
        }

        if (data.name() != null && !data.name().isEmpty()) {
            existingEnterprise.setName(data.name());
        }

        if (data.description() != null && !data.description().isEmpty()) {
            existingEnterprise.setDescription(data.description());
        }

        if (data.city() != null && !data.city().isEmpty()) {
            existingEnterprise.setCity(data.city());
        }

        enterpriseDAO.save(existingEnterprise);
    }

    public void deleteEnterpriseById(UUID id) {
        if (!enterpriseDAO.existsById(id)) {
            log.error("Attempt to delete non-existing Enterprise with ID={}", id);
            throw new NotFoundException("Enterprise not found");
        }

        if( hasActiveJobs(id)) {
            log.error("Cannot delete Enterprise with ID={} because it has active jobs", id);
            throw new ConflictException("Cannot delete enterprise with active jobs");
        }

        enterpriseDAO.deleteById(id);
    }

    private boolean hasActiveJobs(UUID enterpriseId) {
        Enterprise enterprise = enterpriseDAO.findById(enterpriseId)
                .orElseThrow(() -> new NotFoundException("Enterprise not found"));

        return enterprise.getJobs().stream()
                .anyMatch(job -> job.getApplicationDeadline().isAfter(LocalDateTime.now()));
    }
}
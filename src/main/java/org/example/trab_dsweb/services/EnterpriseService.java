package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.repositories.EnterpriseRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class EnterpriseService {
    private BCryptPasswordEncoder encoder;
    private final EnterpriseRepository enterpriseRepository;

    public ReturnEnterpriseDTO createEnterprise(CreateEnterpriseDTO data){
        enterpriseRepository.findEnterpriseByEmail(data.email())
                .ifPresent(enterprise -> {
                    log.error("Enterprise with email={} already exists", data.email());
                    throw new ConflictException("Enterprise with this email already exists");
                });

        enterpriseRepository.findEnterpriseByCnpj(data.cnpj())
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

        Enterprise savedEnterprise = enterpriseRepository.save(newEnterprise);

        return new ReturnEnterpriseDTO(
                savedEnterprise.getId(),
                savedEnterprise.getEmail(),
                savedEnterprise.getCnpj(),
                savedEnterprise.getName(),
                savedEnterprise.getDescription(),
                savedEnterprise.getCity()
        );
    }

    public ReturnEnterpriseDTO getEnterpriseById(UUID id) {
        Enterprise enterprise = enterpriseRepository.findById(id)
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

    public void deleteEnterpriseById(UUID id) {
        if (!enterpriseRepository.existsById(id)) {
            log.error("Attempt to delete non-existing Enterprise with ID={}", id);
            throw new NotFoundException("Enterprise not found");
        }
        enterpriseRepository.deleteById(id);
    }

    public ReturnEnterpriseDTO updateEnterpriseById(UUID id, CreateEnterpriseDTO data) {
        Enterprise existingEnterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Enterprise not found with ID={}", id);
                    return new NotFoundException("Enterprise not found");
                });

        if (data.cnpj() != null && !data.cnpj().isEmpty()) {
            enterpriseRepository.findEnterpriseByCnpj(data.cnpj()).ifPresent(ent -> {
                if (!ent.getId().equals(id)) {
                    log.error("Duplicate CNPJ={} on update for Enterprise ID={}", data.cnpj(), id);
                    throw new ConflictException("Enterprise with this CNPJ already exists");
                }
            });
            existingEnterprise.setCnpj(data.cnpj());
        }

        if (data.email() != null && !data.email().isEmpty()) {
            enterpriseRepository.findEnterpriseByEmail(data.email()).ifPresent(ent -> {
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

        Enterprise updatedEnterprise = enterpriseRepository.save(existingEnterprise);

        return new ReturnEnterpriseDTO(
                updatedEnterprise.getId(),
                updatedEnterprise.getEmail(),
                updatedEnterprise.getCnpj(),
                updatedEnterprise.getName(),
                updatedEnterprise.getDescription(),
                updatedEnterprise.getCity()
        );
    }
}
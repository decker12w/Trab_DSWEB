package org.example.trab_dsweb.services;

import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.repositories.EnterpriseRepository;
import org.springframework.stereotype.Service;

@Service
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    //CREATE
    public ReturnEnterpriseDTO createEnterprise(CreateEnterpriseDTO data){

        enterpriseRepository.findEnterpriseByEmail(data.email())
                .orElseThrow(() -> new IllegalArgumentException("Enterprise with this email already exists"));

        enterpriseRepository.findEnterpriseByCnpj(data.cnpj())
                .orElseThrow(() -> new IllegalArgumentException("Enterprise with this CNPJ already exists"));

        Enterprise newEnterprise = new Enterprise();
                newEnterprise.setName(data.name());
                newEnterprise.setEmail(data.email());
                newEnterprise.setCnpj(data.cnpj());

                return new ReturnEnterpriseDTO(
                        enterpriseRepository.save(newEnterprise).getId(),
                        newEnterprise.getEmail(),
                        newEnterprise.getCnpj(),
                        newEnterprise.getName(),
                        newEnterprise.getDescription(),
                        newEnterprise.getCity()
                );
    }

    //READ
    public ReturnEnterpriseDTO getEnterpriseById(UUID id) {
        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enterprise not found"));

        return new ReturnEnterpriseDTO(
                enterprise.getId(),
                enterprise.getEmail(),
                enterprise.getCnpj(),
                enterprise.getName(),
                enterprise.getDescription(),
                enterprise.getCity()
        );
    }

    //DELETE
    public void deleteEnterpriseById(UUID id) {
        if (!enterpriseRepository.existsById(id)) {
            throw new NotFoundException("Enterprise not found");
        }
        enterpriseRepository.deleteById(id);
    }

    //UPDATE
    public ReturnEnterpriseDTO updateEnterpriseById(UUID id, CreateEnterpriseDTO data) {
        Enterprise existingEnterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enterprise not found"));

        if (data.cnpj() != null && !data.cnpj().isEmpty()) {
            enterpriseRepository.findEnterpriseByCnpj(data.cnpj()).ifPresent(enterprise -> {
                if (!enterprise.getId().equals(id)) {
                    throw new ConflictException("Enterprise with this CNPJ already exists");
                }
            });
            existingEnterprise.setCnpj(data.cnpj());
        }

        if (data.email() != null && !data.email().isEmpty()) {
            enterpriseRepository.findEnterpriseByEmail(data.email()).ifPresent(enterprise -> {
                if (!enterprise.getId().equals(id)) {
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
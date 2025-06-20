package org.example.trab_dsweb.services;

import org.example.trab_dsweb.dto.CreateEnterpriseRequestDTO;
import org.example.trab_dsweb.dto.CreateEnterpriseResponseDTO;
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
    public CreateEnterpriseResponseDTO createEnterprise(CreateEnterpriseRequestDTO data){

        enterpriseRepository.findEnterpriseByEmail(data.email())
                .orElseThrow(() -> new IllegalArgumentException("Enterprise with this email already exists"));

        enterpriseRepository.findEnterpriseByCNPJ(data.CNPJ())
                .orElseThrow(() -> new IllegalArgumentException("Enterprise with this CNPJ already exists"));

        Enterprise newEnterprise = new Enterprise(
                data.email();
                data.password();
                data.CNPJ();
                data.name();
                data.description();
                data.city();
        );

        Enterprise savedEnterprise = enterpriseRepository.save(newEnterprise);

        public CreateEnterpriseResponseDTO createEnterprise(CreateEnterpriseRequestDTO data) {
            savedEnterprise.getName()
        }

        //READ
        public GetEnterpriseResponseDTO getEnterpriseById(UUID id) {
            Enterprise enterprise = enterpriseRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Enterprise not found"));

            return new GetEnterpriseResponseDTO(
                    enterprise.getId(),
                    enterprise.getEmail(),
                    enterprise.getCNPJ(),
                    enterprise.name(),
                    enterprise.getDescription(),
                    enterprise.getCity(),
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
        public GetEnterpriseResponseDTO updateEnterpriseById(UUID id, UpdateEnterpriseRequestDTO data) {
            Enterprise existingEnterprise = enterpriseRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Enterprise not found"));

            if (data.CNPJ() != null && !data.CNPJ().isEmpty()) {
                enterpriseRepository.findEnterpriseByCNPJ(data.CNPJ()).ifPresent(enterprise -> {
                    if (!enterprise.getId().equals(id)) {
                        throw new ConflictException("Enterprise with this CNPJ already exists");
                    }
                });
                existingEnterprise.setCNPJ(data.CNPJ());
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

            return new GetEnterpriseResponseDTO(
                    updatedEnterprise.getId(),
                    updatedEnterprise.getEmail(),
                    updatedEnterprise.getCNPJ(),
                    updatedEnterprise.getName(),
                    updatedEnterprise.getDescription(),
                    updatedEnterprise.getCity()
            );
    }
}
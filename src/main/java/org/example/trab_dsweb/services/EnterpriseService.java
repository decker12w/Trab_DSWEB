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
}
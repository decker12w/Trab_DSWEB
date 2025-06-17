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
        )
    }
}
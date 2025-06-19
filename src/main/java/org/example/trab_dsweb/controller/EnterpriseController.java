package org.example.trab_dsweb.controller;

import org.example.trab_dsweb.dto.CreateEnterpriseRequestDTO;
import org.example.trab_dsweb.dto.CreateEnterpriseResponseDTO;
import org.example.trab_dsweb.services.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

// Controller para gerenciar as requisições relacionadas a empresas.
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * Endpoint para criar uma nova empresa.
     * @param data Os dados da empresa a ser criada.
     * @return uma resposta com os dados da empresa criada e status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<CreateEnterpriseResponseDTO> create(@RequestBody CreateEnterpriseRequestDTO data) {
        CreateEnterpriseResponseDTO newEnterprise = enterpriseService.create(data);
        return new ResponseEntity<>(newEnterprise, HttpStatus.CREATED);
    }
}

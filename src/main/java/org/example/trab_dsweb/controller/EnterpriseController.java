package org.example.trab_dsweb.controller;

import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.services.EnterpriseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {
    private EnterpriseService enterpriseService;

    /**
     * Endpoint para criar uma nova empresa.
     * @param data Os dados da empresa a ser criada.
     * @return uma resposta com os dados da empresa criada e status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ReturnEnterpriseDTO> create(@RequestBody CreateEnterpriseDTO data) {
        ReturnEnterpriseDTO newEnterprise = enterpriseService.createEnterprise(data);
        return new ResponseEntity<>(newEnterprise, HttpStatus.CREATED);
    }
}

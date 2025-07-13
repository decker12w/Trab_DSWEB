package org.example.trab_dsweb.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.model.Enterprise;
import org.example.trab_dsweb.service.EnterpriseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/empresas")
public class EnterpriseRestController {
    private final EnterpriseService enterpriseService;

    @PostMapping
    public ResponseEntity<ReturnEnterpriseDTO> createEnterprise(@Valid @RequestBody CreateEnterpriseDTO data) {
        Enterprise enterprise = enterpriseService.createEnterprise(data);
        return ResponseEntity.ok(new ReturnEnterpriseDTO(enterprise));
    }

    @GetMapping
    public ResponseEntity<List<ReturnEnterpriseDTO>> getAllEnterprises() {
        List<ReturnEnterpriseDTO> enterprises = enterpriseService.findAllEnterprises();
        return ResponseEntity.ok(enterprises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnEnterpriseDTO> getEnterpriseById(@PathVariable UUID id) {
        ReturnEnterpriseDTO enterprise = enterpriseService.findEnterpriseById(id);
        return ResponseEntity.ok(enterprise);
    }

    @GetMapping("/cidades/{nome}")
    public ResponseEntity<List<ReturnEnterpriseDTO>> getEnterprisesByCity(@PathVariable String nome) {
        List<ReturnEnterpriseDTO> enterprises = enterpriseService.findEnterprisesByCity(nome);
        return ResponseEntity.ok(enterprises);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEnterpriseById(@PathVariable UUID id, @Valid @RequestBody CreateEnterpriseDTO data) {
        enterpriseService.updateEnterpriseById(id, data);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnterpriseById(@PathVariable UUID id) {
        enterpriseService.deleteEnterpriseById(id);
        return ResponseEntity.noContent().build();
    }
}

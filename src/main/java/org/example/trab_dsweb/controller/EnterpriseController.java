package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.services.EnterpriseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/enterprise")
@AllArgsConstructor
public class EnterpriseController {
    private EnterpriseService enterpriseService;

    @GetMapping("/{id}")
    public ResponseEntity<ReturnEnterpriseDTO> getEnterpriseById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(enterpriseService.getEnterpriseById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<ReturnEnterpriseDTO> create(@RequestBody @Valid CreateEnterpriseDTO data) {
        ReturnEnterpriseDTO createdEnterprise = enterpriseService.createEnterprise(data);
        return new ResponseEntity<>(createdEnterprise, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnEnterpriseDTO> updateEnterprise(
            @PathVariable("id") UUID id,
            @Valid CreateEnterpriseDTO data) {

        ReturnEnterpriseDTO updatedEnterprise = enterpriseService.updateEnterpriseById(id, data);
        return ResponseEntity.ok(updatedEnterprise);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnterprise(@PathVariable("id") UUID id) {
        enterpriseService.deleteEnterpriseById(id);
        return ResponseEntity.noContent().build();
    }


}

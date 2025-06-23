package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.services.EnterpriseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/enterprise")
@AllArgsConstructor
public class EnterpriseController {

    private EnterpriseService enterpriseService;

    @GetMapping("/{id}")
    public ResponseEntity<ReturnEnterpriseDTO> getEnterpriseById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(enterpriseService.getEnterpriseById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReturnEnterpriseDTO>> getAllEnterprises() {
        List<ReturnEnterpriseDTO> enterprises = enterpriseService.listAllEnterprises();
        return ResponseEntity.ok(enterprises);
    }

    @PostMapping("/register")
    public String createEnterprise(@Valid CreateEnterpriseDTO data) {
        enterpriseService.createEnterprise(data);
        return "redirect:/login";
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnEnterpriseDTO> updateEnterprise(
            @PathVariable("id") UUID id,
            @RequestBody @Valid CreateEnterpriseDTO data) {

        ReturnEnterpriseDTO updatedEnterprise = enterpriseService.updateEnterpriseById(id, data);
        return ResponseEntity.ok(updatedEnterprise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnterprise(@PathVariable("id") UUID id) {
        enterpriseService.deleteEnterpriseById(id);
        return ResponseEntity.noContent().build();
    }
}

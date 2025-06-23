package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.services.EnterpriseService;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @PostMapping("/register")
    public ResponseEntity<ReturnEnterpriseDTO> create(@RequestBody @Valid CreateEnterpriseDTO data) {
        ReturnEnterpriseDTO createdEnterprise = enterpriseService.createEnterprise(data);
        return new ResponseEntity<>(createdEnterprise, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public String registerEnterprise(
            @ModelAttribute("enterpriseData") @Valid CreateEnterpriseDTO data,
            BindingResult result,
            ModelMap model,
            RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "enterprise/register";
        }

        try {
            enterpriseService.createEnterprise(data);
        } catch (ConflictException e) {
            result.reject("global.error", e.getMessage());
            return "enterprise/register";
        }

        attr.addFlashAttribute("successMessage", "Empresa cadastrada com sucesso! Faça seu login.");
        return "redirect:/login";
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

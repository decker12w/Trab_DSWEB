package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.enums.Gender;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.services.WorkerService;
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
@RequestMapping("/api/worker")
@AllArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/{id}")
    public ResponseEntity<ReturnWorkerDTO> getWorkerById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReturnWorkerDTO>> getAllWorkers() {
        List<ReturnWorkerDTO> workers = workerService.listAllWorkers();
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/register")
    public String registerWorkerPage(ModelMap model) {
        model.addAttribute("workerData", new CreateWorkerDTO(null, null, null, null, null, null));

        Map<String, String> genderOptions = new LinkedHashMap<>();
        genderOptions.put(Gender.MALE.name(), "Masculino");
        genderOptions.put(Gender.FEMALE.name(), "Feminino");
        genderOptions.put(Gender.OTHER.name(), "Outro");
        model.addAttribute("genderOptions", genderOptions);

        return "worker/register";
    }

    @PostMapping("/register")
    public String registerWorker(@ModelAttribute("workerData") @Valid CreateWorkerDTO data, BindingResult result, ModelMap model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            Map<String, String> genderOptions = new LinkedHashMap<>();
            genderOptions.put(Gender.MALE.name(), "Masculino");
            genderOptions.put(Gender.FEMALE.name(), "Feminino");
            genderOptions.put(Gender.OTHER.name(), "Outro");
            model.addAttribute("genderOptions", genderOptions);
            return "worker/register";
        }

        try {
            workerService.createWorker(data);

        } catch (ConflictException e) {
            result.reject("global.error", e.getMessage());
            Map<String, String> genderOptions = new LinkedHashMap<>();
            genderOptions.put(Gender.MALE.name(), "Masculino");
            genderOptions.put(Gender.FEMALE.name(), "Feminino");
            genderOptions.put(Gender.OTHER.name(), "Outro");
            model.addAttribute("genderOptions", genderOptions);
            return "worker/register";
        }

        attr.addFlashAttribute("successMessage", "Cadastro realizado com sucesso! Faça seu login.");

        return "redirect:/login";
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnWorkerDTO> updateWorker(
            @PathVariable("id") UUID id,
            @RequestBody @Valid CreateWorkerDTO data) {

        ReturnWorkerDTO updatedWorker = workerService.updateWorkerById(id, data);
        return ResponseEntity.ok(updatedWorker);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable("id") UUID id) {
        workerService.deleteWorkerById(id);
        return ResponseEntity.noContent().build();
    }
}
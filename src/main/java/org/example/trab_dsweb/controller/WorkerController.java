package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.services.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping("/register")
    public String createWorker(@Valid CreateWorkerDTO data) {
        workerService.createWorker(data);
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
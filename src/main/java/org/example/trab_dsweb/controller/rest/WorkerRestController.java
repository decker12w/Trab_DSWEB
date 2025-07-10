package org.example.trab_dsweb.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.services.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerRestController {

    private final WorkerService workerService;

    @PostMapping
    public ResponseEntity<ReturnWorkerDTO> createWorker(@Valid @RequestBody CreateWorkerDTO data) {
        Worker worker = workerService.createWorker(data);
        return ResponseEntity.ok(new ReturnWorkerDTO(worker));
    }

    @GetMapping
    public ResponseEntity<List<ReturnWorkerDTO>> getAllWorkers() {
        List<ReturnWorkerDTO> workers = workerService.listAllWorkers();
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnWorkerDTO> getWorkerById(@PathVariable UUID id) {
        ReturnWorkerDTO worker = workerService.findWorkerById(id);
        return ResponseEntity.ok(worker);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkerById(@PathVariable UUID id, @Valid @RequestBody CreateWorkerDTO data) {
        workerService.updateWorkerById(id, data);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkerById(@PathVariable UUID id) {
        workerService.deleteWorkerById(id);
        return ResponseEntity.noContent().build();
    }
}
package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.*;
import org.example.trab_dsweb.services.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/work")
@AllArgsConstructor
public class WorkController {

    private final WorkerService workerService;

    @GetMapping("/{id}")
    public ResponseEntity<GetWorkerResponseDTO> getWorkerById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }

    @PostMapping()
    public ResponseEntity<CreateWorkerResponseDTO> createWorker(@RequestBody CreateWorkerRequestDTO data) {
        CreateWorkerResponseDTO createdWorker = workerService.createWorker(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdWorker.id()).toUri();
        return ResponseEntity.created(uri).body(createdWorker);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetWorkerResponseDTO> updateWorker(
            @PathVariable("id") UUID id,
            @RequestBody UpdateWorkerRequestDTO data) {

        GetWorkerResponseDTO updatedWorker = workerService.updateWorkerById(id, data);
        return ResponseEntity.ok(updatedWorker);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable("id") UUID id) {
        workerService.deleteWorkerById(id);
        return ResponseEntity.noContent().build();
    }
}
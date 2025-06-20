package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.services.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/job")
@AllArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<ReturnJobDTO> createJob(@RequestBody @Valid CreateJobDTO data) {
        ReturnJobDTO response = jobService.createJob(data);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(response.id())
                        .toUri())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReturnJobDTO>> findAllActiveJobsByCity(@RequestParam String city) {
        List<ReturnJobDTO> jobs = jobService.findAllActiveJobsByCity(city);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ReturnJobDTO>> getJobById(@PathVariable("id") UUID id) {
        List<ReturnJobDTO> job = jobService.findAllJobsByEnterpriseId(id);
        return ResponseEntity.ok(job);
    }
}
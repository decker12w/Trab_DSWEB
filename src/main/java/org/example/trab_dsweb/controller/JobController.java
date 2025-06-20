package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobRequestDTO;
import org.example.trab_dsweb.dto.GetJobResponseDTO;
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
    public ResponseEntity<GetJobResponseDTO> createJob(@RequestBody CreateJobRequestDTO data) {
        GetJobResponseDTO response = jobService.createJob(data);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(response.id())
                        .toUri())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<GetJobResponseDTO>> findAllActiveJobsByCity(@RequestParam String city) {
        List<GetJobResponseDTO> jobs = jobService.findAllActiveJobsByCity(city);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<GetJobResponseDTO>> getJobById(@PathVariable("id") UUID id) {
        List<GetJobResponseDTO> job = jobService.findAllJobsByEnterpriseId(id);
        return ResponseEntity.ok(job);
    }
}
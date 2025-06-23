package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.UpdateJobApplicationStatusDTO;
import org.example.trab_dsweb.services.JobApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-application")
@AllArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<ReturnJobApplicationDTO> createJobApplication(@RequestBody @Valid CreateJobApplicationDTO data) {
        ReturnJobApplicationDTO createdJobApplication = jobApplicationService.createJobApplication(data);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(createdJobApplication.id())
                        .toUri())
                .body(createdJobApplication);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReturnJobApplicationDTO> updateJobApplicationStatus(@PathVariable UUID id, @RequestBody @Valid UpdateJobApplicationStatusDTO data) {
        ReturnJobApplicationDTO updateJobApplication = jobApplicationService.updateJobApplicationStatus(id, data);
        return ResponseEntity.ok(updateJobApplication);
    }

    @GetMapping("/worker/{id}")
    public ResponseEntity<List<ReturnJobApplicationDTO>> findAllJobApplicationsByWorkerId(@PathVariable("id") UUID id) {
        List<ReturnJobApplicationDTO> jobApplications = jobApplicationService.findAllJobApplicationsByWorkerId(id);
        return ResponseEntity.ok(jobApplications);
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<List<ReturnJobApplicationDTO>> findAllJobApplicationsByJobId(@PathVariable("id") UUID id) {
        List<ReturnJobApplicationDTO> jobApplications = jobApplicationService.findAllJobApplicationsByJobId(id);
        return ResponseEntity.ok(jobApplications);
    }
}

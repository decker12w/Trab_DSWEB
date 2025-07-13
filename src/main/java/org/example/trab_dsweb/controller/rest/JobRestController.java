package org.example.trab_dsweb.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.model.Job;
import org.example.trab_dsweb.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vagas")
public class JobRestController {
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<ReturnJobDTO>> getAllJobs() {
        List<ReturnJobDTO> jobs = jobService.findAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/empresas/{id}")
    public ResponseEntity<List<ReturnJobDTO>> getAllActiveJobsByEnterprise(@PathVariable UUID id) {
        List<ReturnJobDTO> jobs = jobService.findAllActiveJobsByEnterpriseId(id);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnJobDTO> getJobById(@PathVariable UUID id) {
        ReturnJobDTO job = jobService.findById(id);
        return ResponseEntity.ok(job);
    }
}

package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.UpdateJobApplicationStatusDTO;
import org.example.trab_dsweb.services.JobApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/job-application")
@AllArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<ReturnJobApplicationDTO> createJobApplication(@Valid CreateJobApplicationDTO data) {
        ReturnJobApplicationDTO createdJobApplication = jobApplicationService.createJobApplication(data);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(createdJobApplication.id())
                        .toUri())
                .body(createdJobApplication);
    }

    @PostMapping("/{id}/status")
    public String updateJobApplicationStatus(@PathVariable UUID id, @Valid UpdateJobApplicationStatusDTO data) {
        jobApplicationService.updateJobApplicationStatus(id, data);
        return "redirect:/api/job-application/job/" + id;
    }

    @GetMapping("/worker/{id}")
    public String findAllJobApplicationsByWorkerId(@PathVariable("id") UUID id, ModelMap model) {
        List<ReturnJobApplicationDTO> jobApplications = jobApplicationService.findAllJobApplicationsByWorkerId(id);
        model.addAttribute("jobApplications", jobApplications);
        return "worker/job-application/list";
    }

    @GetMapping("/job/{id}")
    public String findAllJobApplicationsByJobId(@PathVariable("id") UUID id, ModelMap model) {
        List<ReturnJobApplicationDTO> jobApplications = jobApplicationService.findAllJobApplicationsByJobId(id);
        model.addAttribute("jobApplications", jobApplications);
        return "job/job-application/list";
    }
}

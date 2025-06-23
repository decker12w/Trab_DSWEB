package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.UpdateJobApplicationStatusDTO;
import org.example.trab_dsweb.services.JobApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/job-application")
@AllArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping
    public String createJobApplication(@RequestParam("jobId") UUID jobId, @RequestParam("curriculum") MultipartFile curriculum, RedirectAttributes attr) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        CreateJobApplicationDTO data = new CreateJobApplicationDTO(email, jobId, curriculum);

        jobApplicationService.createJobApplication(data);
        attr.addFlashAttribute("successMessage", "Candidatura realizada com sucesso!");
        return "redirect:/api/home";
    }

    @PostMapping("/{id}/status")
    public String updateJobApplicationStatus(@PathVariable UUID id, @Valid UpdateJobApplicationStatusDTO data) {
        jobApplicationService.updateJobApplicationStatus(id, data);
        return "redirect:/api/job-application/job/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteJobApplicationById(@PathVariable UUID id) {
        jobApplicationService.deleteJobApplicationById(id);
        return "redirect:/api/job-application/worker";
    }

    @GetMapping("/worker")
    public String findAllJobApplicationsByWorker(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<ReturnJobApplicationDTO> jobApplications = jobApplicationService.findAllJobApplicationsByWorkerEmail(email);
        model.addAttribute("jobApplications", jobApplications);
        return "worker/dashboard";
    }

    @GetMapping("/job/{id}")
    public String findAllJobApplicationsByJobId(@PathVariable("id") UUID id, ModelMap model) {
        List<ReturnJobApplicationDTO> jobApplications = jobApplicationService.findAllJobApplicationsByJobId(id);
        model.addAttribute("jobApplications", jobApplications);
        return "job/job-applications";
    }
}

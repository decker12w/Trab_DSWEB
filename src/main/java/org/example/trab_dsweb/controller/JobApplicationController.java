package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.UpdateJobApplicationStatusDTO;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.security.WorkerDetails;
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
@RequestMapping("/job-applications")
@AllArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping
    public String createJobApplication(@RequestParam("jobId") UUID jobId, @RequestParam("curriculum") MultipartFile curriculum, RedirectAttributes attr) {
        Worker worker = getLoggedWorker();
        CreateJobApplicationDTO data = new CreateJobApplicationDTO(curriculum, worker.getId(), jobId);
        jobApplicationService.createJobApplication(data);
        attr.addFlashAttribute("successMessage", "Candidatura realizada com sucesso!");
        return "redirect:/home";
    }

    @PostMapping("/{id}/status")
    public String updateJobApplicationStatus(@PathVariable UUID id, @Valid UpdateJobApplicationStatusDTO data) {
        jobApplicationService.updateJobApplicationStatus(id, data);
        return "redirect:/job-applications/job/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteJobApplicationById(@PathVariable UUID id) {
        jobApplicationService.deleteJobApplicationById(id);
        return "redirect:/job-applications/worker";
    }

    @GetMapping("/worker")
    public String findAllJobApplicationsByWorker(ModelMap model) {
        Worker worker = getLoggedWorker();
        List<ReturnJobApplicationDTO> jobApplications = jobApplicationService.findAllJobApplicationsByWorkerId(worker.getId());
        model.addAttribute("jobApplications", jobApplications);
        return "worker/dashboard";
    }

    private Worker getLoggedWorker() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof WorkerDetails workerDetails)) {
            throw new Error("error");
        }
        return workerDetails.getWorker();
    }
}

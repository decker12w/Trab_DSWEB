package org.example.trab_dsweb.controller;

import jakarta.persistence.Cacheable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.UpdateJobApplicationStatusDTO;
import org.example.trab_dsweb.enums.Status;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.exceptions.exceptions.UnauthorizedException;
import org.example.trab_dsweb.models.JobApplication;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.security.WorkerDetails;
import org.example.trab_dsweb.services.JobApplicationService;
import org.example.trab_dsweb.services.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/job-applications")
@AllArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping
    public String createJobApplication(@RequestParam("jobId") UUID jobId, @RequestParam("curriculum") MultipartFile curriculum, RedirectAttributes attr) {
      try {
          Worker worker = getLoggedWorker();
          CreateJobApplicationDTO data = new CreateJobApplicationDTO(curriculum, worker.getId(), jobId);
          jobApplicationService.createJobApplication(data);
          attr.addFlashAttribute("successMessage", "Candidatura realizada com sucesso!");
          return "redirect:/home";
      }
      catch (ConflictException e) {
          attr.addFlashAttribute("errorMessage", "Você já se candidatou para esta vaga.");
      }
      return "redirect:/home";
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

    private byte[] getJobApplication(UUID id) {
        ReturnJobApplicationDTO jobApplication = jobApplicationService.getJobApplicationById(id);
        return jobApplication.curriculum();
    }

    @GetMapping(value = "/download/{id}")
    public void download(HttpServletResponse response, @PathVariable("id") UUID id) {
        try {
            byte[] curriculum = getJobApplication(id);

            response.setContentType("application/pdf");

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=\"curriculo_" + id + ".pdf\"";
            response.setHeader(headerKey, headerValue);

            response.getOutputStream().write(curriculum);
            response.getOutputStream().flush();

        } catch (IOException e) {
            System.out.println("Error :- " + e.getMessage());
        }
    }
    private Worker getLoggedWorker() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof WorkerDetails workerDetails)) {
            throw new UnauthorizedException("Worker is not logged in");
        }
        return workerDetails.getWorker();
    }

    @PostMapping("/{id}/status")
    public String updateJobApplicationStatus(@PathVariable UUID id,
                                             @RequestParam("status") Status status,
                                             @RequestParam("jobId") UUID jobId,
                                             RedirectAttributes redirectAttributes) {
        UpdateJobApplicationStatusDTO data = new UpdateJobApplicationStatusDTO(status, null);
        jobApplicationService.updateJobApplicationStatus(id, data);
        redirectAttributes.addFlashAttribute("successMessage", "Candidate status updated successfully!");
        return "redirect:/enterprises/jobs/" + jobId + "/analysis";
    }

}

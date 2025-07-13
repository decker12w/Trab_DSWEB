package org.example.trab_dsweb.controller.mvc;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.UpdateJobApplicationStatusDTO;
import org.example.trab_dsweb.exception.exceptions.BadRequestException;
import org.example.trab_dsweb.exception.exceptions.ConflictException;
import org.example.trab_dsweb.exception.exceptions.InternalServerErrorException;
import org.example.trab_dsweb.exception.exceptions.UnauthorizedException;
import org.example.trab_dsweb.indicator.Status;
import org.example.trab_dsweb.model.Worker;
import org.example.trab_dsweb.security.WorkerDetails;
import org.example.trab_dsweb.service.JobApplicationService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    @PostMapping
    public String createJobApplication(@RequestParam("jobId") UUID jobId, @RequestParam("curriculum") MultipartFile curriculum, RedirectAttributes redirectAttributes) {
      try {
          Worker worker = getLoggedWorker();
          CreateJobApplicationDTO data = new CreateJobApplicationDTO(curriculum, worker.getId(), jobId);
          jobApplicationService.createJobApplication(data);
          String successMessage = messageSource.getMessage("success.jobApplication.register", null, LocaleContextHolder.getLocale());
          redirectAttributes.addFlashAttribute("successMessage", successMessage);
          return "redirect:/home";
      } catch (BadRequestException | ConflictException e) {
          redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
          return "redirect:/home";
      }
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
            throw new InternalServerErrorException(messageSource.getMessage("error.jobApplication.internal.download", null, LocaleContextHolder.getLocale()));
        }
    }

    @PostMapping("/{id}/status")
    public String updateJobApplicationStatus(@PathVariable UUID id,
                                             @RequestParam("status") Status status,
                                             @RequestParam(value = "link", required = false) String link,
                                             @RequestParam("jobId") UUID jobId,
                                             RedirectAttributes redirectAttributes) {
        UpdateJobApplicationStatusDTO data = new UpdateJobApplicationStatusDTO(status, link);
        jobApplicationService.updateJobApplicationStatus(id, data);
        String successMessage = messageSource.getMessage("success.jobApplication.edit", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/enterprises/jobs/" + jobId + "/analysis";
    }

    private Worker getLoggedWorker() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof WorkerDetails workerDetails)) {
            throw new UnauthorizedException(messageSource.getMessage("error.jobApplicaiton.unauthorized.worker", null, LocaleContextHolder.getLocale()));
        }
        return workerDetails.getWorker();
    }

    private byte[] getJobApplication(UUID id) {
        ReturnJobApplicationDTO jobApplication = jobApplicationService.getJobApplicationById(id);
        return jobApplication.curriculum();
    }
}

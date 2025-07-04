package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.enums.Status;
import org.example.trab_dsweb.exceptions.exceptions.UnauthorizedException;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.security.EnterpriseDetails;
import org.example.trab_dsweb.services.EnterpriseService;
import org.example.trab_dsweb.services.JobApplicationService;
import org.example.trab_dsweb.services.JobService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/enterprises")
@AllArgsConstructor
public class EnterpriseController {
    private final EnterpriseService enterpriseService;
    private final JobService jobService;
    private final JobApplicationService jobApplicationService;

    @GetMapping("/register")
    public String showRegisterEnterpriseForm(Model model) {
        model.addAttribute("enterpriseData", new CreateEnterpriseDTO(null, null, null, null, null, null));
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/enterprises/register");
        return "enterprise/form";
    }

    @PostMapping("/register")
    public String processRegisterEnterprise(@ModelAttribute("enterpriseData") CreateEnterpriseDTO enterpriseData, RedirectAttributes redirectAttributes) {
        try {
            enterpriseService.createEnterprise(enterpriseData);
            redirectAttributes.addFlashAttribute("successMessage", "Entreprise registered successfully!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/enterprises/register";
        }
    }

    @GetMapping("/dashboard")
    public String showEnterpriseDashboard(Model model) {
        Enterprise enterprise = getLoggedEnterprise();
        List<ReturnJobDTO> jobs = jobService.findAllJobsByEnterpriseId(enterprise.getId());
        model.addAttribute("enterpriseName", enterprise.getName());
        model.addAttribute("jobs", jobs);
        return "enterprise/dashboard";
    }

    private Enterprise getLoggedEnterprise() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof EnterpriseDetails enterpriseDetails)) {
            throw new UnauthorizedException("Enterprise is not logged in");
        }
        return enterpriseDetails.getEnterprise();
    }

    @GetMapping("/jobs/{jobId}/analysis")
    public String showJobAnalysisPage(@PathVariable("jobId") UUID jobId, Model model) {
        Enterprise enterprise = getLoggedEnterprise();

        ReturnJobDTO job = jobService.findById(jobId);

        if (!job.enterprise().id().equals(enterprise.getId())) {
            throw new AccessDeniedException("Access denied: You do not have permission to view this job analysis.");
        }

        List<ReturnJobApplicationDTO> applications = jobApplicationService.findByJobId(jobId);

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        model.addAttribute("statusOptions", Status.values());
        model.addAttribute("link", null);

        return "enterprise/analysis";
    }
}
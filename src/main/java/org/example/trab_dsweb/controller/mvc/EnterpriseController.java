package org.example.trab_dsweb.controller.mvc;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.indicator.Status;
import org.example.trab_dsweb.exception.exceptions.UnauthorizedException;
import org.example.trab_dsweb.model.Enterprise;
import org.example.trab_dsweb.security.EnterpriseDetails;
import org.example.trab_dsweb.service.EnterpriseService;
import org.example.trab_dsweb.service.JobApplicationService;
import org.example.trab_dsweb.service.JobService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/enterprises")
@AllArgsConstructor
public class EnterpriseController {
    private final EnterpriseService enterpriseService;
    private final JobService jobService;
    private final JobApplicationService jobApplicationService;

    @GetMapping("/dashboard")
    public String showEnterpriseDashboard(Model model) {
        Enterprise enterprise = getLoggedEnterprise();
        List<ReturnJobDTO> jobs = jobService.findAllJobsByEnterpriseId(enterprise.getId());
        model.addAttribute("enterpriseName", enterprise.getName());
        model.addAttribute("jobs", jobs);
        return "enterprise/dashboard";
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

    private Enterprise getLoggedEnterprise() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof EnterpriseDetails enterpriseDetails)) {
            throw new UnauthorizedException("Enterprise is not logged in");
        }
        return enterpriseDetails.getEnterprise();
    }
}
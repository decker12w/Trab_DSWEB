package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobDTO;
import org.example.trab_dsweb.indicator.JobType;
import org.example.trab_dsweb.exception.exceptions.UnauthorizedException;
import org.example.trab_dsweb.model.Enterprise;
import org.example.trab_dsweb.security.EnterpriseDetails;
import org.example.trab_dsweb.service.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jobs")
@AllArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/register")
    public String showRegisterJobForm(Model model) {
        if (!model.containsAttribute("jobData")) {
            model.addAttribute("jobData", new CreateJobDTO(null, null, null, null, null, null, null));
        }
        Map<String, String> jobTypeMessageKeys = Arrays.stream(JobType.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        jobTypeEnum -> "fragments.jobCard.job.type." + jobTypeEnum.name().toLowerCase()
                ));
        model.addAttribute("jobTypeOptions", jobTypeMessageKeys.entrySet());

        return "job/form";
    }


    @PostMapping("/register")
    public String processRegisterJob(@Valid @ModelAttribute("jobData") CreateJobDTO jobData,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.jobData", bindingResult);
            redirectAttributes.addFlashAttribute("jobData", jobData);

            return "redirect:/jobs/register";
        }

        try {
            Enterprise enterprise = getLoggedEnterprise();
            jobService.createJob(jobData, enterprise.getId());

            redirectAttributes.addFlashAttribute("successMessage", "Vaga criada com sucesso!");
            return "redirect:/enterprises/dashboard";
        } catch (Exception e) {
            System.out.println("Error creating job: " + e.getMessage());
            redirectAttributes.addFlashAttribute("jobData", jobData);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/jobs/register";
        }
    }

    private Enterprise getLoggedEnterprise() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof EnterpriseDetails enterpriseDetails)) {
            throw new UnauthorizedException("Enterprise is not logged in");
        }
        return enterpriseDetails.getEnterprise();
    }
}
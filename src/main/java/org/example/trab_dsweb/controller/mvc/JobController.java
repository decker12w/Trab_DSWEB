package org.example.trab_dsweb.controller.mvc;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobDTO;
import org.example.trab_dsweb.exception.exceptions.BadRequestException;
import org.example.trab_dsweb.exception.exceptions.ConflictException;
import org.example.trab_dsweb.exception.exceptions.UnauthorizedException;
import org.example.trab_dsweb.indicator.JobType;
import org.example.trab_dsweb.model.Enterprise;
import org.example.trab_dsweb.security.EnterpriseDetails;
import org.example.trab_dsweb.service.JobService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    @GetMapping("/register")
    public String showRegisterJobForm(Model model) {
        if (!model.containsAttribute("jobData")) {
            model.addAttribute("jobData", new CreateJobDTO(null, null, null, null, null, null, null));
        }
        model.addAttribute("jobTypeOptions", getJobTypeKeys().entrySet());
        return "job/form";
    }

    @PostMapping("/register")
    public String processRegisterJob(@Valid @ModelAttribute("jobData") CreateJobDTO jobData, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.jobData", bindingResult);
                redirectAttributes.addFlashAttribute("jobData", jobData);
                return "redirect:/jobs/register";
            }
            Enterprise enterprise = getLoggedEnterprise();
            jobService.createJob(jobData, enterprise.getId());
            String successMessage = messageSource.getMessage("success.job.register", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/enterprises/dashboard";
        } catch (BadRequestException | ConflictException e) {
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

    private Map<String, String> getJobTypeKeys() {
        return Arrays.stream(JobType.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        jobTypeEnum -> "fragments.jobCard.job.type." + jobTypeEnum.name().toLowerCase()
                ));
    }
}
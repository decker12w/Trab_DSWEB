package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobDTO;
import org.example.trab_dsweb.enums.JobType;
import org.example.trab_dsweb.services.JobService;
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
@RequestMapping("/api/jobs")
@AllArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/register")
    public String showRegisterJobForm(Model model) {
        if (!model.containsAttribute("jobData")) {
            model.addAttribute("jobData", new CreateJobDTO(null, null, null, null, null, null, null, null));
        }


        model.addAttribute("formAction", "/api/jobs/register");
        addJobTypeOptionsToModel(model);

        return "job-form";
    }

    @PostMapping("/register")
    public String processRegisterJob(@Valid @ModelAttribute("jobData") CreateJobDTO jobData,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.jobData", bindingResult);
            redirectAttributes.addFlashAttribute("jobData", jobData);

            return "redirect:/api/jobs/register";
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            jobService.createJob(jobData, email);

            redirectAttributes.addFlashAttribute("successMessage", "Vaga criada com sucesso!");
            return "redirect:/api/enterprise/dashboard";
        } catch (Exception e) {
            System.out.println("Error creating job: " + e.getMessage());
            redirectAttributes.addFlashAttribute("jobData", jobData);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/api/jobs/register";
        }
    }

    private void addJobTypeOptionsToModel(Model model) {
        Map<String, String> jobTypeOptions = Arrays.stream(JobType.values())
                .collect(Collectors.toMap(Enum::name, JobType::getDisplayName));
        model.addAttribute("jobTypeOptions", jobTypeOptions.entrySet());
    }
}
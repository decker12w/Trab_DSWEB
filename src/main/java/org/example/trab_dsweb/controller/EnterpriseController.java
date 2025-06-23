package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.security.EnterpriseDetails;
import org.example.trab_dsweb.services.EnterpriseService;
import org.example.trab_dsweb.services.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/enterprises")
@AllArgsConstructor
public class EnterpriseController {
    private final EnterpriseService enterpriseService;
    private final JobService jobService;

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
            redirectAttributes.addFlashAttribute("successMessage", "Empresa cadastrada com sucesso!");
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
            throw new Error("error");
        }
        return enterpriseDetails.getEnterprise();
    }
}
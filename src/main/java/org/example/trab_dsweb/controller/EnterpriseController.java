package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.repositories.EnterpriseRepository;
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
@RequestMapping("/api/enterprise")
@AllArgsConstructor
public class EnterpriseController {

    private final EnterpriseService enterpriseService;
    private final EnterpriseRepository enterpriseRepository;
    private final JobService jobService;

    @GetMapping("/register")
    public String showRegisterEnterpriseForm(Model model) {
        model.addAttribute("enterpriseData", new CreateEnterpriseDTO(null, null, null, null, null, null));
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/api/enterprise/register");
        return "enterprise-form";
    }

    @PostMapping("/register")
    public String processRegisterEnterprise(@ModelAttribute("enterpriseData") CreateEnterpriseDTO enterpriseData, RedirectAttributes redirectAttributes) {
        try {
            enterpriseService.createEnterprise(enterpriseData);
            redirectAttributes.addFlashAttribute("successMessage", "Empresa cadastrada com sucesso!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/api/enterprise/register";
        }
    }

    @GetMapping("/dashboard")
    public String showEnterpriseDashboard(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Enterprise enterprise = enterpriseRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada para o e-mail: " + email));

        List<ReturnJobDTO> jobs = jobService.findAllJobsByEnterpriseEmail(email);

        model.addAttribute("enterpriseName", enterprise.getName());
        model.addAttribute("jobs", jobs);

        return "enterprise-dashboard";
    }
}
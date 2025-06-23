package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.repositories.EnterpriseRepository;
import org.example.trab_dsweb.services.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final JobService jobService;
    private final EnterpriseRepository enterpriseRepository;


    @GetMapping("/enterprise")
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
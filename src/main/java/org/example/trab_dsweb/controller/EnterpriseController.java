package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.services.EnterpriseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enterprise")
@AllArgsConstructor
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    @GetMapping("/register")
    public String showRegisterEnterpriseForm(Model model) {
        model.addAttribute("enterpriseData", new CreateEnterpriseDTO(null, null, null, null, null, null));
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/enterprise/register");
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
            return "redirect:/enterprise/register";
        }
    }
}
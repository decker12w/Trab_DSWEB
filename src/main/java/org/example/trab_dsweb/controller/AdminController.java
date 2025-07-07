package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.indicator.Gender;
import org.example.trab_dsweb.service.EnterpriseService;
import org.example.trab_dsweb.service.WorkerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admins")
@AllArgsConstructor
public class AdminController {
    private final WorkerService workerService;
    private final EnterpriseService enterpriseService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("workers", workerService.listAllWorkers());
        model.addAttribute("enterprises", enterpriseService.findAllEnterprises());
        return "admin/dashboard";
    }

    @GetMapping("/workers/register")
    public String showRegisterWorkerForm(Model model) {
        model.addAttribute("workerData", new CreateWorkerDTO(null, null, null, null, null, null));
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/admins/workers/register");
        Map<String, String> genderMessageKeys = Arrays.stream(Gender.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        genderEnum -> "gender." + genderEnum.name().toLowerCase()
                ));
        model.addAttribute("genderOptions", genderMessageKeys.entrySet());
        return "worker/form";
    }

    @PostMapping("/workers/register")
    public String processRegisterWorker(@ModelAttribute("workerData") CreateWorkerDTO workerData, RedirectAttributes redirectAttributes) {
        try {
            workerService.createWorker(workerData);
            redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admins/workers/register";
        }
    }

    @GetMapping("/enterprises/register")
    public String showRegisterEnterpriseForm(Model model) {
        model.addAttribute("enterpriseData", new CreateEnterpriseDTO(null, null, null, null, null, null));
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/admins/enterprises/register");
        return "enterprise/form";
    }

    @PostMapping("/enterprises/register")
    public String processRegisterEnterprise(@ModelAttribute("enterpriseData") CreateEnterpriseDTO enterpriseData, RedirectAttributes redirectAttributes) {
        try {
            enterpriseService.createEnterprise(enterpriseData);
            redirectAttributes.addFlashAttribute("successMessage", "Entreprise registered successfully!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admins/enterprises/register";
        }
    }

    @GetMapping("/workers/edit/{id}")
    public String showEditWorkerForm(@PathVariable UUID id, Model model) {
        ReturnWorkerDTO workerDTO = workerService.findWorkerById(id);
        CreateWorkerDTO workerData = new CreateWorkerDTO(
                workerDTO.email(), null, workerDTO.cpf(), workerDTO.name(), workerDTO.gender(), workerDTO.birthDate()
        );

        model.addAttribute("workerData", workerData);
        model.addAttribute("isEdit", true);
        model.addAttribute("formAction", "/admins/workers/edit/" + id);

        Map<String, String> genderOptions = Arrays.stream(Gender.values())
                .collect(Collectors.toMap(Enum::name, Gender::getDisplayName));
        model.addAttribute("genderOptions", genderOptions.entrySet());

        return "worker/form";
    }

    @PostMapping("/workers/edit/{id}")
    public String processEditWorkerForm(@PathVariable UUID id, @ModelAttribute("workerData") CreateWorkerDTO workerData, RedirectAttributes redirectAttributes) {
        workerService.updateWorkerById(id, workerData);
        redirectAttributes.addFlashAttribute("successMessage", "Profissional atualizado com sucesso!");
        return "redirect:/admins/dashboard";
    }


    @GetMapping("/workers/delete/{id}")
    public String deleteWorker(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        workerService.deleteWorkerById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Profissional excluído com sucesso!");
        return "redirect:/admins/dashboard";
    }

    @GetMapping("/enterprises/edit/{id}")
    public String showEditEnterpriseForm(@PathVariable UUID id, Model model) {
        ReturnEnterpriseDTO enterpriseDTO = enterpriseService.findEnterpriseById(id);
        CreateEnterpriseDTO enterpriseData = new CreateEnterpriseDTO(
                enterpriseDTO.email(), null, enterpriseDTO.cnpj(), enterpriseDTO.name(), enterpriseDTO.description(), enterpriseDTO.city()
        );

        model.addAttribute("enterpriseData", enterpriseData);
        model.addAttribute("isEdit", true);
        model.addAttribute("formAction", "/admins/enterprises/edit/" + id);
        return "enterprise/form";
    }

    @PostMapping("/enterprises/edit/{id}")
    public String processEditEnterpriseForm(@PathVariable UUID id, @ModelAttribute("enterpriseData") CreateEnterpriseDTO enterpriseData, RedirectAttributes redirectAttributes) {
        enterpriseService.updateEnterpriseById(id, enterpriseData);
        redirectAttributes.addFlashAttribute("successMessage", "Empresa atualizada com sucesso!");
        return "redirect:/admins/dashboard";
    }

    @GetMapping("/enterprises/delete/{id}")
    public String deleteEnterprise(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        enterpriseService.deleteEnterpriseById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Empresa excluída com sucesso!");
        return "redirect:/admins/dashboard";
    }
}
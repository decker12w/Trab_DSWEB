package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.enums.Gender;
import org.example.trab_dsweb.services.EnterpriseService;
import org.example.trab_dsweb.services.WorkerService;
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

    @GetMapping("/worker/edit/{id}")
    public String showEditWorkerForm(@PathVariable UUID id, Model model) {
        ReturnWorkerDTO workerDTO = workerService.findWorkerById(id);
        CreateWorkerDTO workerData = new CreateWorkerDTO(
                workerDTO.email(), null, workerDTO.cpf(), workerDTO.name(), workerDTO.gender(), workerDTO.birthDate()
        );

        model.addAttribute("workerData", workerData);
        model.addAttribute("isEdit", true);
        model.addAttribute("formAction", "/admins/worker/edit/" + id);

        Map<String, String> genderOptions = Arrays.stream(Gender.values())
                .collect(Collectors.toMap(Enum::name, Gender::getDisplayName));
        model.addAttribute("genderOptions", genderOptions.entrySet());

        return "worker/form";
    }


    @PostMapping("/worker/edit/{id}")
    public String processEditWorkerForm(@PathVariable UUID id, @ModelAttribute("workerData") CreateWorkerDTO workerData, RedirectAttributes redirectAttributes) {
        workerService.updateWorkerById(id, workerData);
        redirectAttributes.addFlashAttribute("successMessage", "Profissional atualizado com sucesso!");
        return "redirect:/admins/dashboard";
    }


    @GetMapping("/worker/delete/{id}")
    public String deleteWorker(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        workerService.deleteWorkerById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Profissional excluído com sucesso!");
        return "redirect:/admins/dashboard";
    }


    @GetMapping("/enterprise/edit/{id}")
    public String showEditEnterpriseForm(@PathVariable UUID id, Model model) {
        ReturnEnterpriseDTO enterpriseDTO = enterpriseService.findEnterpriseById(id);
        CreateEnterpriseDTO enterpriseData = new CreateEnterpriseDTO(
                enterpriseDTO.email(), null, enterpriseDTO.cnpj(), enterpriseDTO.name(), enterpriseDTO.description(), enterpriseDTO.city()
        );

        model.addAttribute("enterpriseData", enterpriseData);
        model.addAttribute("isEdit", true);
        model.addAttribute("formAction", "/admins/enterprise/edit/" + id);
        return "enterprise/form";
    }


    @PostMapping("/enterprise/edit/{id}")
    public String processEditEnterpriseForm(@PathVariable UUID id, @ModelAttribute("enterpriseData") CreateEnterpriseDTO enterpriseData, RedirectAttributes redirectAttributes) {
        enterpriseService.updateEnterpriseById(id, enterpriseData);
        redirectAttributes.addFlashAttribute("successMessage", "Empresa atualizada com sucesso!");
        return "redirect:/admins/dashboard";
    }


    @GetMapping("/enterprise/delete/{id}")
    public String deleteEnterprise(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        enterpriseService.deleteEnterpriseById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Empresa excluída com sucesso!");
        return "redirect:/admins/dashboard";
    }
}
package org.example.trab_dsweb.controller.mvc;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.indicator.Gender;
import org.example.trab_dsweb.service.EnterpriseService;
import org.example.trab_dsweb.service.WorkerService;
import org.example.trab_dsweb.exception.exceptions.ConflictException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final MessageSource messageSource;

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
        return "admin/work-form";
    }

    @PostMapping("/workers/register")
    public String processRegisterWorker(@Valid @ModelAttribute("workerData") CreateWorkerDTO workerData, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("formAction", "/admins/workers/register");

            Map<String, String> genderMessageKeys = Arrays.stream(Gender.values())
                    .collect(Collectors.toMap(
                            Enum::name,
                            genderEnum -> "gender." + genderEnum.name().toLowerCase()
                    ));
            model.addAttribute("genderOptions", genderMessageKeys.entrySet());

            return "admin/work-form";
        }
        try {
            workerService.createWorker(workerData);
            String successMessage = messageSource.getMessage("success.worker.register", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/admins/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ocorreu um erro inesperado: " + e.getMessage());
            return "redirect:/admins/workers/register";
        }
    }

    @GetMapping("/enterprises/register")
    public String showRegisterEnterpriseForm(Model model) {
        model.addAttribute("enterpriseData", new CreateEnterpriseDTO(null, null, null, null, null, null));
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/admins/enterprises/register");
        return "admin/entreprise-form";
    }

    @PostMapping("/enterprises/register")
    public String processRegisterEnterprise(@Valid @ModelAttribute("enterpriseData") CreateEnterpriseDTO enterpriseData, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("formAction", "/admins/enterprises/register");
            return "admin/entreprise-form";
        }

        try {
            enterpriseService.createEnterprise(enterpriseData);
            String successMessage = messageSource.getMessage("success.enterprise.register", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/admins/dashboard";
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

        Map<String, String> genderMessageKeys = Arrays.stream(Gender.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        genderEnum -> "gender." + genderEnum.name().toLowerCase()
                ));
        model.addAttribute("genderOptions", genderMessageKeys.entrySet());

        return "admin/work-form";
    }

    @PostMapping("/workers/edit/{id}")
    public String processEditWorkerForm(@PathVariable UUID id, @ModelAttribute("workerData") CreateWorkerDTO workerData, RedirectAttributes redirectAttributes) {
        workerService.updateWorkerById(id, workerData);
        String successMessage = messageSource.getMessage("success.worker.edit", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/admins/dashboard";
    }


    @GetMapping("/workers/delete/{id}")
    public String deleteWorker(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        workerService.deleteWorkerById(id);
        String successMessage = messageSource.getMessage("success.worker.delete", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
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
        return "admin/entreprise-form";
    }

    @PostMapping("/enterprises/edit/{id}")
    public String processEditEnterpriseForm(@PathVariable UUID id, @ModelAttribute("enterpriseData") CreateEnterpriseDTO enterpriseData, RedirectAttributes redirectAttributes) {
        enterpriseService.updateEnterpriseById(id, enterpriseData);
        String successMessage = messageSource.getMessage("success.enterprise.edit", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/admins/dashboard";
    }

    @GetMapping("/enterprises/delete/{id}")
    public String deleteEnterprise(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            enterpriseService.deleteEnterpriseById(id);
            String successMessage = messageSource.getMessage("success.enterprise.delete", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (Exception e) { // Captura qualquer exceção genérica
            String errorMessage;

            if (e instanceof ConflictException) {
                errorMessage = messageSource.getMessage("error.enterprise.delete.conflict", null, LocaleContextHolder.getLocale());
            } else {
                errorMessage = e.getMessage();
            }

            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/admins/dashboard";
    }
}
package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.enums.Gender;
import org.example.trab_dsweb.services.WorkerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/worker")
@AllArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/register")
    public String showRegisterWorkerForm(Model model) {

        model.addAttribute("workerData", new CreateWorkerDTO(null, null, null, null, null, null));
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/worker/register");


        Map<String, String> genderOptions = Arrays.stream(Gender.values())
                .collect(Collectors.toMap(Enum::name, Gender::getDisplayName));
        model.addAttribute("genderOptions", genderOptions.entrySet());

        return "worker-form";
    }

    @PostMapping("/register")
    public String processRegisterWorker(@ModelAttribute("workerData") CreateWorkerDTO workerData, RedirectAttributes redirectAttributes) {
        try {
            workerService.createWorker(workerData);
            redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/worker/register";
        }
    }
}
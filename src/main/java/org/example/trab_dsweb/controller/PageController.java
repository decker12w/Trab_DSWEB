package org.example.trab_dsweb.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateWorkerDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.enums.Gender;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.services.JobService;
import org.example.trab_dsweb.services.WorkerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class PageController {

    private final JobService jobService;
    private final WorkerService workerService;

    @GetMapping("/")
    public String showHomePage(Model model, @RequestParam(name = "city", required = false) String city) {
        List<ReturnJobDTO> jobs;
        if (city != null && !city.isBlank()) {
            jobs = jobService.findAllActiveJobsByCity(city);
        } else {
            jobs = jobService.finAllActiveJobs();
        }
        model.addAttribute("jobs", jobs);
        return "index";
    }

    @GetMapping("/register/worker")
    public String registerWorkerPage(Model model) {
        model.addAttribute("workerData", new CreateWorkerDTO(null, null, null, null, null, null));

        Map<String, String> genderOptions = new LinkedHashMap<>();
        genderOptions.put(Gender.MALE.name(), "Masculino");
        genderOptions.put(Gender.FEMALE.name(), "Feminino");
        genderOptions.put(Gender.OTHER.name(), "Outro");
        model.addAttribute("genderOptions", genderOptions);

        return "register-worker";
    }

    @PostMapping("/register/worker")
    public String processRegistration(
            @Valid @ModelAttribute("workerData") CreateWorkerDTO workerData,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            Map<String, String> genderOptions = new LinkedHashMap<>();
            genderOptions.put(Gender.MALE.name(), "Masculino");
            genderOptions.put(Gender.FEMALE.name(), "Feminino");
            genderOptions.put(Gender.OTHER.name(), "Outro");
            model.addAttribute("genderOptions", genderOptions);
            return "register-worker";
        }

        try {
            workerService.createWorker(workerData);

        } catch (ConflictException e) {
            bindingResult.reject("global.error", e.getMessage());
            Map<String, String> genderOptions = new LinkedHashMap<>();
            genderOptions.put(Gender.MALE.name(), "Masculino");
            genderOptions.put(Gender.FEMALE.name(), "Feminino");
            genderOptions.put(Gender.OTHER.name(), "Outro");
            model.addAttribute("genderOptions", genderOptions);
            return "register-worker";
        }


        redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso! Faça seu login.");

        return "redirect:/login";
    }
}
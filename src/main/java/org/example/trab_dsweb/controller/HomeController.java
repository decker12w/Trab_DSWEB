package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.services.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/home")
@AllArgsConstructor
public class HomeController {
    private final JobService jobService;

    @GetMapping
    public String showHomePage(Model model, @RequestParam(name = "city", required = false) String city) {
        List<ReturnJobDTO> jobs;
        if (city != null && !city.isBlank()) {
            jobs = jobService.findAllActiveJobsByCity(city);
        } else {
            jobs = jobService.findAllActiveJobs();
        }
        model.addAttribute("jobs", jobs);
        return "index";
    }
}
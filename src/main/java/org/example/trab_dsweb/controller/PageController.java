package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.services.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Este Controller é responsável por RENDERIZAR PÁGINAS HTML com Thymeleaf.
 * Ele não retorna JSON, ele retorna o nome do template.
 */
@Controller
@AllArgsConstructor
public class PageController {

    private final JobService jobService;

    /**
     * Este método lida com as requisições para a página inicial do site.
     * Ele busca os dados das vagas e os adiciona ao Model para que o Thymeleaf possa usá-los.
     * @param model O objeto "mala" para carregar dados para a view.
     * @param city  Um parâmetro opcional da URL para filtrar vagas (ex: /?city=Sao%20Paulo)
     * @return O nome do arquivo HTML a ser renderizado (ex: "index.html")
     */
    @GetMapping("/")
    public String showHomePage(Model model, @RequestParam(name = "city", required = false) String city) {

        List<ReturnJobDTO> jobs;

        // ESTA É A LÓGICA PRINCIPAL:
        // Se o usuário passou uma cidade na URL...
        if (city != null && !city.isBlank()) {
            // ...chama o serviço que busca por cidade.
            jobs = jobService.findAllActiveJobsByCity(city);
        } else {
            // ...senão, chama o serviço que busca todas as vagas ativas.
            jobs = jobService.finAllActiveJobs();
        }

        // Adiciona a lista (filtrada ou não) ao modelo com o nome "jobs".
        // Este nome "jobs" é o que o th:each="job : ${jobs}" no HTML vai usar.
        model.addAttribute("jobs", jobs);

        // Retorna o nome do arquivo da view que o Thymeleaf deve renderizar.
        // Se seu arquivo se chama home.html, retorne "home".
        // Se seu arquivo se chama index.html, retorne "index".
        return "index";
    }
}
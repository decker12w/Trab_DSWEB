package org.example.trab_dsweb.controller;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.dto.ReturnWorkerDTO;
import org.example.trab_dsweb.dto.ReturnWorkerInJobDTO;
import org.example.trab_dsweb.dto.ReturnJobApplicationDTO;
import org.example.trab_dsweb.services.JobService;
import org.example.trab_dsweb.services.JobApplicationService;
import org.example.trab_dsweb.services.WorkerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.trab_dsweb.enums.JobType;
import org.example.trab_dsweb.enums.Status;
import org.example.trab_dsweb.dto.ReturnEnterpriseInJobDTO;
import java.time.LocalDateTime;



/**
 * Este Controller é responsável por RENDERIZAR PÁGINAS HTML com Thymeleaf.
 * Ele não retorna JSON, ele retorna o nome do template.
 */
@Controller
@AllArgsConstructor
public class PageController {

    private final JobService jobService;
    private final WorkerService workerService;
    private final JobApplicationService jobApplicationService;

    /**
     * Este método lida com as requisições para a página inicial do site.
     * Ele busca os dados das vagas e os adiciona ao Model para que o Thymeleaf possa usá-los.
     *
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
            jobs = jobService.findAllActiveJobs();
        }

        // Adiciona a lista (filtrada ou não) ao modelo com o nome "jobs".
        // Este nome "jobs" é o que o th:each="job : ${jobs}" no HTML vai usar.
        model.addAttribute("jobs", jobs);

        // Retorna o nome do arquivo da view que o Thymeleaf deve renderizar.
        // Se seu arquivo se chama home.html, retorne "home".
        // Se seu arquivo se chama index.html, retorne "index".
        return "index";
    }

    @GetMapping("/CadastroEmpresa")
    public String enterpriseRegister() {
        return "CadastroEmpresa";
    }

    @GetMapping("/PainelEmpresa/{id}")
    public String enterpriseDashboard(@PathVariable("id") UUID id, Model model) {
        List<ReturnJobDTO> jobs = jobService.findAllJobsByEnterpriseId(id);
        model.addAttribute("nome", "Maria da Silva");
        model.addAttribute("candidaturas", jobs);
        return "painelEnterprise";
    }

    @GetMapping("/analiseCandidatos/{vagaId}")
    public String showCandidateAnalysis(
            @RequestParam("vagaId") UUID vagaId,
            Model model) {
        List<ReturnJobApplicationDTO> candidatos = jobApplicationService.findAllJobApplicationsByJobId(vagaId);
        String tituloVaga = jobService.findTitleById(vagaId);
        model.addAttribute("candidatos", candidatos);
        model.addAttribute("tituloVaga", tituloVaga);
        return "analiseCandidatos";
    }


    @GetMapping("/PainelEmpresaFake")
    public String painelEmpresaFake(Model model) {
        // Crie alguns DTOs fake
        List<ReturnJobDTO> jobs = List.of(
                new ReturnJobDTO(
                        UUID.randomUUID(),
                        "Desenvolvedor Frontend",
                        "12.345.678/0001-99",
                        JobType.INTERNSHIP,
                        new ReturnEnterpriseInJobDTO(UUID.randomUUID(), "Empresa Fake", "12.345.678/0001-99"),
                        LocalDateTime.now().plusDays(10),
                        true,
                        4000.0,
                        List.of("React", "CSS", "Tailwind"),
                        "São Paulo",                  // city
                        "Desenvolvedor Frontend",     // title (pode repetir ou variar)
                        15                            // numOfCandidates (exemplo mock)
                ),
                new ReturnJobDTO(
                        UUID.randomUUID(),
                        "Analista de Dados",
                        "98.765.432/0001-11",
                        JobType.FULL_TIME,
                        new ReturnEnterpriseInJobDTO(UUID.randomUUID(), "Outra Empresa", "98.765.432/0001-11"),
                        LocalDateTime.now().plusDays(40),
                        true,
                        6500.0,
                        List.of("SQL", "PowerBI", "Excel"),
                        "Belo Horizonte",             // city
                        "Analista de Dados",          // title
                        8                             // numOfCandidates
                )
        );
        model.addAttribute("nomeEmpresa", "Empresa Teste");
        model.addAttribute("vagas", jobs);
        return "painelEnterprise";
    }



    @GetMapping("/analiseCandidatosFake")
    public String analiseCandidatosFake(Model model) {
        List<ReturnJobApplicationDTO> candidatos = List.of(
                new ReturnJobApplicationDTO(
                        UUID.randomUUID(),
                        Status.OPEN,
                        new ReturnWorkerInJobDTO(
                                UUID.randomUUID(),
                                "João da Silva",
                                "111.222.333-44" // CPF ou outro campo do seu DTO
                        ),
                        null
                ),
                new ReturnJobApplicationDTO(
                        UUID.randomUUID(),
                        Status.UNSELECTED,
                        new ReturnWorkerInJobDTO(
                                UUID.randomUUID(),
                                "Maria Oliveira",
                                "222.333.444-55"
                        ),
                        null
                ),
                new ReturnJobApplicationDTO(
                        UUID.randomUUID(),
                        Status.INTERVIEW,
                        new ReturnWorkerInJobDTO(
                                UUID.randomUUID(),
                                "Carlos Souza",
                                "333.444.555-66"
                        ),
                        null
                ),
                new ReturnJobApplicationDTO(
                        UUID.randomUUID(),
                        Status.OPEN,
                        new ReturnWorkerInJobDTO(
                                UUID.randomUUID(),
                                "Paula Martins",
                                "444.555.666-77"
                        ),
                        null
                )
        );
        String tituloVaga = "Desenvolvedor Backend Pleno (FAKE)";
        model.addAttribute("candidatos", candidatos);
        model.addAttribute("tituloVaga", tituloVaga);
        return "analiseCandidatos";
    }
}
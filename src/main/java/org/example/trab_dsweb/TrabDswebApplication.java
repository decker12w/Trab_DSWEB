package org.example.trab_dsweb;

import org.example.trab_dsweb.daos.AdminDAO;
import org.example.trab_dsweb.daos.EnterpriseDAO;
import org.example.trab_dsweb.daos.JobDAO;
import org.example.trab_dsweb.enums.JobType;
import org.example.trab_dsweb.models.Admin;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.models.Job;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TrabDswebApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrabDswebApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(AdminDAO adminDAO, EnterpriseDAO enterpriseDAO, JobDAO jobDAO, BCryptPasswordEncoder passwordEncoder) {
        return (args) -> {
            String password = passwordEncoder.encode("123456");

            Admin admin = new Admin();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(password);

            adminDAO.save(admin);

            Enterprise enterprise1 = new Enterprise();
            enterprise1.setCnpj("12.345.678/0001-1");
            enterprise1.setEmail("techcorp@gmail.com");
            enterprise1.setPassword(password);
            enterprise1.setName("Tech Corp");
            enterprise1.setDescription("Empresa de tecnologia");
            enterprise1.setCity("Londrina");

            Enterprise enterprise2 = new Enterprise();
            enterprise2.setCnpj("34.567.890/0001-33");
            enterprise2.setEmail("edusol@email.com");
            enterprise2.setPassword(password);
            enterprise2.setName("Edu Solutions");
            enterprise2.setDescription("Plataforma de ensino online");
            enterprise2.setCity("Maringá");

            enterprise1 = enterpriseDAO.save(enterprise1);
            enterprise2 = enterpriseDAO.save(enterprise2);

            Job job1 = new Job();
            job1.setTitle("Desenvolvedor Backend Java");
            job1.setDescription("Junte-se a uma equipe ágil e colaborativa que valoriza boas práticas e qualidade de código.");
            job1.setJobType(JobType.FULL_TIME);
            job1.setApplicationDeadline(LocalDateTime.now().plusWeeks(2));
            job1.setRemuneration(6000.0);
            job1.setCity("Londrina");
            job1.setEnterprise(enterprise1);
            List<String> job1Skills = Arrays.asList(
                    "Java (8+)",
                    "Spring Boot",
                    "APIs RESTful",
                    "Banco de Dados SQL (PostgreSQL, MySQL)",
                    "JPA / Hibernate",
                    "Versionamento com Git",
                    "Testes unitários e integração (JUnit, Mockito)",
                    "Docker",
                    "CI/CD (Jenkins, GitHub Actions)",
                    "Boas práticas de Clean Code e SOLID"
            );
            job1.setSkills(job1Skills);

            Job job2 = new Job();
            job2.setTitle("Desenvolvedor Full Stack");
            job2.setDescription("Venha evoluir com a gente em um ambiente leve, ágil e colaborativo!");
            job2.setJobType(JobType.INTERNSHIP);
            job2.setApplicationDeadline(LocalDateTime.now().plusWeeks(3));
            job2.setRemuneration(1800.0);
            job2.setCity("Maringá");
            job2.setEnterprise(enterprise2);
            List<String> job2Skills = Arrays.asList(
                    "Lógica de Programação",
                    "HTML, CSS e JavaScript",
                    "Noções de React ou Vue.js",
                    "Conhecimentos básicos de APIs REST",
                    "Git e GitHub",
                    "Curiosidade e vontade de aprender",
                    "Capacidade de trabalhar em equipe",
                    "Boa comunicação",
                    "Organização com tarefas e prazos"
            );
            job2.setSkills(job2Skills);

            jobDAO.save(job1);
            jobDAO.save(job2);
        };
    }
}

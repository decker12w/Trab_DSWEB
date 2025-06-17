package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobRequestDTO;
import org.example.trab_dsweb.dto.GetJobResponseDTO;
import org.example.trab_dsweb.models.Entreprise;
import org.example.trab_dsweb.models.Job;
import org.example.trab_dsweb.repositories.EntrepriseRepository;
import org.example.trab_dsweb.repositories.JobRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobService {
    private JobRepository jobRepository;
    private EntrepriseRepository entrepriseRepository;

    public Job createJob(CreateJobRequestDTO createJobRequestDTO) {
        Job job = new Job();
        job.setDescription(createJobRequestDTO.description());
        job.setJobType(createJobRequestDTO.jobType());
        job.setCNPJ(createJobRequestDTO.CNPJ());
        job.setApplicationDeadline(createJobRequestDTO.applicationDeadline());
        job.setJobActive(true);
        job.setCity(createJobRequestDTO.city());

        Entreprise entreprise = entrepriseRepository.findById(createJobRequestDTO.enterpriseId())
                .orElseThrow(() -> new IllegalArgumentException("Entreprise not found with ID: " + createJobRequestDTO.enterpriseId()));

        job.setEntreprise(entreprise);

        return jobRepository.save(job);
    }

    public List<GetJobResponseDTO> findALlActiveJobsByCity(String city) {
        return jobRepository.findByJobActiveTrueAndCity(city).stream()
                .map(job -> new GetJobResponseDTO(
                        job.getId(),
                        job.getDescription(),
                        job.getCNPJ(),
                        job.getJobType(),
                        job.getEntreprise(),
                        job.getApplicationDeadline(),
                        job.isJobActive(),
                        job.getCity()))
                .collect(Collectors.toList());
    }

    public List<GetJobResponseDTO> findAllJobs() {
        return jobRepository.findAll().stream()
                .map(job -> new GetJobResponseDTO(
                        job.getId(),
                        job.getDescription(),
                        job.getCNPJ(),
                        job.getJobType(),
                        job.getEntreprise(),
                        job.getApplicationDeadline(),
                        job.isJobActive(),
                        job.getCity()))
                .collect(Collectors.toList());
    }
}
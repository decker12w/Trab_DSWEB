package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobRequestDTO;
import org.example.trab_dsweb.dto.GetJobResponseDTO;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Entreprise;
import org.example.trab_dsweb.models.Job;
import org.example.trab_dsweb.repositories.EntrepriseRepository;
import org.example.trab_dsweb.repositories.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobService {
    private JobRepository jobRepository;
    private EntrepriseRepository entrepriseRepository;

    public GetJobResponseDTO createJob(CreateJobRequestDTO createJobRequestDTO) {
        Job job = new Job();
        job.setDescription(createJobRequestDTO.description());
        job.setJobType(createJobRequestDTO.jobType());
        job.setCNPJ(createJobRequestDTO.CNPJ());
        job.setApplicationDeadline(createJobRequestDTO.applicationDeadline());
        job.setJobActive(true);
        job.setSkills(createJobRequestDTO.skills());
        job.setRemuneration(createJobRequestDTO.remuneration());
        job.setCity(createJobRequestDTO.city());

        Entreprise entreprise = entrepriseRepository.findById(createJobRequestDTO.enterpriseId())
                .orElseThrow(() -> new NotFoundException("Entreprise not found with ID: " + createJobRequestDTO.enterpriseId()));

        job.setEntreprise(entreprise);
        Job savedJob = jobRepository.save(job);

        return GetJobResponseDTO.mapJobToDTO(savedJob);
    }

    @Transactional(readOnly = true)
    public List<GetJobResponseDTO> findAllActiveJobsByCity(String city) {
        return jobRepository.findByJobActiveTrueAndCity(city).stream()
                .map(GetJobResponseDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GetJobResponseDTO> findAllJobsByEnterpriseId(UUID id) {
        return jobRepository.findAllByEntrepriseId(id).stream()
                .map(GetJobResponseDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }


}

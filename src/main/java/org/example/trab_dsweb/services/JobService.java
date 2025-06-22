package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dto.CreateJobDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.models.Job;
import org.example.trab_dsweb.repositories.EnterpriseRepository;
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
    private EnterpriseRepository enterpriseRepository;

    public ReturnJobDTO createJob(CreateJobDTO createJobRequestDTO) {
        Job job = new Job();
        job.setDescription(createJobRequestDTO.description());
        job.setJobType(createJobRequestDTO.jobType());
        job.setCNPJ(createJobRequestDTO.CNPJ());
        job.setApplicationDeadline(createJobRequestDTO.applicationDeadline());
        job.setJobActive(true);
        job.setSkills(createJobRequestDTO.skills());
        job.setRemuneration(createJobRequestDTO.remuneration());
        job.setCity(createJobRequestDTO.city());

        Enterprise enterprise = enterpriseRepository.findById(createJobRequestDTO.enterpriseId())
                .orElseThrow(() -> new NotFoundException("Enterprise not found with ID: " + createJobRequestDTO.enterpriseId()));

        job.setEnterprise(enterprise);
        Job savedJob = jobRepository.save(job);

        return ReturnJobDTO.mapJobToDTO(savedJob);
    }
    public List<ReturnJobDTO> finAllActiveJobs(){
        return jobRepository.findAllActiveTrue().stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReturnJobDTO> findAllActiveJobsByCity(String city) {
        return jobRepository.findByJobActiveTrueAndCity(city).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReturnJobDTO> findAllJobsByEnterpriseId(UUID id) {
        return jobRepository.findAllByEnterpriseId(id).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }
}

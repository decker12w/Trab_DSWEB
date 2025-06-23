package org.example.trab_dsweb.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dto.CreateJobDTO;
import org.example.trab_dsweb.dto.ReturnJobDTO;
import org.example.trab_dsweb.exceptions.exceptions.NotFoundException;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.models.Job;
import org.example.trab_dsweb.repositories.EnterpriseRepository;
import org.example.trab_dsweb.repositories.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class JobService {
    private JobRepository jobRepository;
    private EnterpriseRepository enterpriseRepository;

    public List<ReturnJobDTO> findAllActiveJobs(){
        return jobRepository.findAllByJobActiveTrue().stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    public List<ReturnJobDTO> findAllActiveJobsByCity(String city) {
        return jobRepository.findAllByJobActiveTrueAndCityContainingIgnoreCase(city).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    public List<ReturnJobDTO> findAllJobsByEnterpriseId(UUID enterpriseId) {
        return jobRepository.findAllByEnterpriseId(enterpriseId).stream()
                .map(ReturnJobDTO::mapJobToDTO)
                .collect(Collectors.toList());
    }

    public void createJob(CreateJobDTO createJobRequestDTO, UUID enterpriseId) {
        Job job = new Job();
        job.setDescription(createJobRequestDTO.description());
        job.setTitle(createJobRequestDTO.title());
        job.setJobType(createJobRequestDTO.jobType());
        job.setApplicationDeadline(createJobRequestDTO.applicationDeadline());
        job.setJobActive(true);
        job.setSkills(createJobRequestDTO.skills());
        job.setRemuneration(createJobRequestDTO.remuneration());
        job.setCity(createJobRequestDTO.city());

        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> {
                    log.error("Enterprise not found with ID={}", enterpriseId);
                    return new NotFoundException("Enterprise not found");
                });
        job.setEnterprise(enterprise);

        jobRepository.save(job);
    }
}

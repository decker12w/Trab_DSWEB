package org.example.trab_dsweb.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trab_dsweb.dao.EnterpriseDAO;
import org.example.trab_dsweb.dao.JobDAO;
import org.example.trab_dsweb.dto.CreateEnterpriseDTO;
import org.example.trab_dsweb.dto.ReturnEnterpriseDTO;
import org.example.trab_dsweb.exception.exceptions.ConflictException;
import org.example.trab_dsweb.exception.exceptions.NotFoundException;
import org.example.trab_dsweb.model.Enterprise;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class EnterpriseService {
    private final JobDAO jobDAO;
    private BCryptPasswordEncoder encoder;
    private final EnterpriseDAO enterpriseDAO;
    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Transactional(readOnly = true)
    public List<ReturnEnterpriseDTO> findAllEnterprises() {
        return StreamSupport.stream(enterpriseDAO.findAll().spliterator(), false)
                .map(enterprise -> new ReturnEnterpriseDTO(
                        enterprise.getId(),
                        enterprise.getEmail(),
                        enterprise.getCnpj(),
                        enterprise.getName(),
                        enterprise.getDescription(),
                        enterprise.getCity()
                ))
                .collect(Collectors.toList());
    }

    public List<ReturnEnterpriseDTO> listAllEnterprises() {
        return StreamSupport.stream(enterpriseDAO.findAll().spliterator(), false)
                .map(enterprise -> new ReturnEnterpriseDTO(
                        enterprise.getId(),
                        enterprise.getCnpj(),
                        enterprise.getEmail(),
                        enterprise.getName(),
                        enterprise.getDescription(),
                        enterprise.getCity()))
                .toList();
    }

    public ReturnEnterpriseDTO findEnterpriseById(UUID id) {
        Enterprise enterprise = enterpriseDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("Enterprise not found with ID={}", id);
                    return new NotFoundException(messageSource.getMessage("error.enterprise.notfound", null, LocaleContextHolder.getLocale()));
                });

        return new ReturnEnterpriseDTO(
                enterprise.getId(),
                enterprise.getEmail(),
                enterprise.getCnpj(),
                enterprise.getName(),
                enterprise.getDescription(),
                enterprise.getCity()
        );
    }

    public List<ReturnEnterpriseDTO> findEnterprisesByCity(String cityName) {
        List<Enterprise> enterprises = enterpriseDAO.findAllByCity(cityName);
        return enterprises.stream()
                .map(ReturnEnterpriseDTO::new)
                .toList();
    }

    public Enterprise createEnterprise(CreateEnterpriseDTO data){
        enterpriseDAO.findByCnpj(data.cnpj())
                .ifPresent(enterprise -> {
                    log.error("Enterprise with CNPJ={} already exists", data.cnpj());
                    throw new ConflictException(messageSource.getMessage("error.enterprise.conflict.cnpj", null, LocaleContextHolder.getLocale()));
                });

        enterpriseDAO.findByEmail(data.email())
                .ifPresent(enterprise -> {
                    log.error("Enterprise with email={} already exists", data.email());
                    throw new ConflictException(messageSource.getMessage("error.enterprise.conflict.email", null, LocaleContextHolder.getLocale()));
                });

        Enterprise newEnterprise = new Enterprise();
        newEnterprise.setName(data.name());
        newEnterprise.setEmail(data.email());
        newEnterprise.setCnpj(data.cnpj());
        newEnterprise.setPassword(encoder.encode(data.password()));
        newEnterprise.setDescription(data.description());
        newEnterprise.setCity(data.city());

        enterpriseDAO.save(newEnterprise);
        return newEnterprise;
    }

    @Transactional
    public void updateEnterpriseById(UUID id, CreateEnterpriseDTO data) {
        Enterprise existingEnterprise = enterpriseDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("Enterprise not found with ID={}", id);
                    return new NotFoundException(messageSource.getMessage("error.enterprise.notfound", null, locale));
                });

        if (data.cnpj() != null && !data.cnpj().isEmpty()) {
            enterpriseDAO.findByCnpj(data.cnpj()).ifPresent(ent -> {
                if (!ent.getId().equals(id)) {
                    log.error("Duplicate CNPJ={} on update for Enterprise ID={}", data.cnpj(), id);
                    throw new ConflictException(messageSource.getMessage("error.enterprise.conflict.cnpj", null, locale));
                }
            });
            existingEnterprise.setCnpj(data.cnpj());
        }

        if (data.email() != null && !data.email().isEmpty()) {
            enterpriseDAO.findByEmail(data.email()).ifPresent(ent -> {
                if (!ent.getId().equals(id)) {
                    log.error("Duplicate email={} on update for Enterprise ID={}", data.email(), id);
                    throw new ConflictException(messageSource.getMessage("error.enterprise.conflict.email", null, locale));
                }
            });
            existingEnterprise.setEmail(data.email());
        }

        if (data.password() != null && !data.password().isEmpty()) {
            existingEnterprise.setPassword(data.password());
        }

        if (data.name() != null && !data.name().isEmpty()) {
            existingEnterprise.setName(data.name());
        }

        if (data.description() != null && !data.description().isEmpty()) {
            existingEnterprise.setDescription(data.description());
        }

        if (data.city() != null && !data.city().isEmpty()) {
            existingEnterprise.setCity(data.city());
        }

        enterpriseDAO.save(existingEnterprise);
    }

    public void deleteEnterpriseById(UUID id) {
        if (!enterpriseDAO.existsById(id)) {
            log.error("Attempt to delete non-existing Enterprise with ID={}", id);
            throw new NotFoundException(messageSource.getMessage("error.enterprise.notfound", null, locale));
        }
        if (!jobDAO.findAllByEnterpriseId(id).isEmpty()) {
            log.error("Cannot delete Enterprise with ID={} because it has jobs", id);
            throw new ConflictException(messageSource.getMessage("error.enterprise.delete.conflict", null, locale));
        }
        enterpriseDAO.deleteById(id);
    }
}
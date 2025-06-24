package org.example.trab_dsweb.dto;
import java.util.UUID;


public record ReturnEnterpriseInJobDTO(
        UUID id,
        String name,
        String cnpj
) {}

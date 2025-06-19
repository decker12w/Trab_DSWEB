package org.example.trab_dsweb.dto;
import java.util.UUID;


public record EnterpriseInJobResponseDTO(
        UUID id,
        String name,
        String cnpj
) {}

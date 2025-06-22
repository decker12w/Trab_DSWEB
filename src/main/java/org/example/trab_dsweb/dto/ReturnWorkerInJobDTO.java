package org.example.trab_dsweb.dto;

import java.util.UUID;

public record ReturnWorkerInJobDTO(
        UUID id,
        String name,
        String cpf
) {}

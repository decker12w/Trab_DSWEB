package org.example.trab_dsweb.dto;

import org.example.trab_dsweb.enums.Gender;
import org.example.trab_dsweb.models.Worker;

import java.time.LocalDate;
import java.util.UUID;

public record ReturnWorkerDTO(
        UUID id,
        String email,
        String cpf,
        String name,
        Gender gender,
        LocalDate birthDate
) {
    public ReturnWorkerDTO(Worker worker) {
        this(
                worker.getId(),
                worker.getEmail(),
                worker.getCpf(),
                worker.getName(),
                worker.getGender(),
                worker.getBirthDate()
        );
    }
}

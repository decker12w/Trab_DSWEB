package org.example.trab_dsweb.daos;

import org.example.trab_dsweb.models.Enterprise;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface EnterpriseDAO extends CrudRepository<Enterprise, UUID> {
    Optional<Enterprise> findByCnpj(String cnpj);
    Optional<Enterprise> findByEmail(String email);
}
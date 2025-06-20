package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnterpriseRepository extends CrudRepository<Enterprise, UUID> {
    Optional<Enterprise> findEnterpriseByCNPJ(String CNPJ);
    Optional<Enterprise> findEnterpriseByEmail(String email);
    }

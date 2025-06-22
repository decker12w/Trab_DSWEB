package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.Enterprise;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;
import java.util.UUID;

public interface EnterpriseRepository extends CrudRepository<Enterprise, UUID> {
    Optional<Enterprise> findEnterpriseByCnpj(String cnpj);
    Optional<Enterprise> findEnterpriseByEmail(String email);
}
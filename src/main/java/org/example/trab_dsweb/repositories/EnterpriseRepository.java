package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EnterpriseRepository extends JpaRepository<Enterprise, UUID> {
    Optional<Enterprise> findEnterpriseByCnpj(String cnpj);
    Optional<Enterprise> findEnterpriseByEmail(String email);
}
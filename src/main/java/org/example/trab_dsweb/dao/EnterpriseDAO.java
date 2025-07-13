package org.example.trab_dsweb.dao;

import org.example.trab_dsweb.model.Enterprise;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnterpriseDAO extends CrudRepository<Enterprise, UUID> {
    Optional<Enterprise> findByCnpj(String cnpj);
    Optional<Enterprise> findByEmail(String email);
    List<Enterprise> findAllByCity(String city);
}
package org.example.trab_dsweb.repositories;

import org.example.trab_dsweb.models.Entreprise;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EntrepriseRepository extends CrudRepository<Entreprise, UUID> {
    Entreprise findByCnpj(String cnpj);
}

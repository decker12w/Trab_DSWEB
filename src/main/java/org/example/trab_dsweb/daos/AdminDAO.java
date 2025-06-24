package org.example.trab_dsweb.daos;

import org.example.trab_dsweb.models.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminDAO extends CrudRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}

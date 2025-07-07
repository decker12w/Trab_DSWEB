package org.example.trab_dsweb.dao;

import org.example.trab_dsweb.model.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminDAO extends CrudRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}

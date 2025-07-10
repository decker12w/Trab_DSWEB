package org.example.trab_dsweb.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.trab_dsweb.dao.WorkerDAO;
import org.example.trab_dsweb.models.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class UniqueCPFValidator implements ConstraintValidator<UniqueCPF, String> {

    @Autowired
    private WorkerDAO dao;

    @Override
    public boolean isValid(String CPF, ConstraintValidatorContext context) {
        if (CPF == null || dao == null) {
            return false;
        }

        Optional<Worker> worker = dao.findByCpf(CPF);
        return worker.isEmpty();
    }
}
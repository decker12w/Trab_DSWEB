package org.example.trab_dsweb.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.validator.UniqueCPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class UniqueCPFValidator implements ConstraintValidator<UniqueCPF, String> {

    @Autowired
    private org.example.trab_dsweb.daos.WorkerDAO dao;

    @Override
    public boolean isValid(String CPF, ConstraintValidatorContext context) {
        if (dao != null) {
            Optional<Worker> worker = dao.findByCpf(CPF);

            return worker.isPresent() ? false : true;
        }
        return false;
    }
}
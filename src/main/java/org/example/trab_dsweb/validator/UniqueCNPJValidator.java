package org.example.trab_dsweb.validator;

import org.example.trab_dsweb.dao.EnterpriseDAO;
import org.example.trab_dsweb.models.Enterprise;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired; // <-- IMPORT THIS
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UniqueCNPJValidator implements ConstraintValidator<UniqueCNPJ, String> {
    @Autowired
    private EnterpriseDAO dao;

    public UniqueCNPJValidator() {
    }

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null || dao == null) {
            return true;
        }

        Optional<Enterprise> enterprise = dao.findByCnpj(cnpj);
        return enterprise.isEmpty();
    }
}
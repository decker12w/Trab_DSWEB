package org.example.trab_dsweb.validator;// org/example/trab_dsweb/validator/UniqueCNPJValidator.java

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.trab_dsweb.dao.EnterpriseDAO;
import org.example.trab_dsweb.models.Enterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UniqueCNPJValidator implements ConstraintValidator<UniqueCNPJ, Enterprise> { // <<< Altere para Enterprise

    @Autowired
    private EnterpriseDAO dao;

    @Override
    public boolean isValid(Enterprise enterprise, ConstraintValidatorContext context) {
        if (enterprise == null || enterprise.getCnpj() == null) {
            return false;
        }

        Optional<Enterprise> existingEnterprise = dao.findByCnpj(enterprise.getCnpj());

        if (existingEnterprise.isEmpty()) {
            return true;
        }

        return enterprise.getId() != null && enterprise.getId().equals(existingEnterprise.get().getId());
    }
}
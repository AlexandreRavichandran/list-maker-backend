package com.medialistmaker.appuser.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class CustomEntityValidator<T> {

    public List<String> validateEntity(T entityToValidate) {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> errorList = validator.validate(entityToValidate);

        return errorList.isEmpty() ?
                new ArrayList<>() :
                this.getInvalidFieldAndValue(errorList);
    }

    private List<String> getInvalidFieldAndValue(Set<ConstraintViolation<T>> errorList) {

        List<String> errors = new ArrayList<>();

        for (ConstraintViolation<T> error : errorList) {
            errors.add(error.getMessage());
        }

        return errors;

    }
}

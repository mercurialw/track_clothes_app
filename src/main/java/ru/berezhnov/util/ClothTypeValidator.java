package ru.berezhnov.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.berezhnov.models.ClothType;
import ru.berezhnov.services.ClothTypeService;

@Component
public class ClothTypeValidator implements Validator {

    private final ClothTypeService clothTypeService;

    @Autowired
    public ClothTypeValidator(final ClothTypeService clothTypeService) {
        this.clothTypeService = clothTypeService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ClothTypeValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ClothType clothType = (ClothType) target;
        if (clothTypeService.findByName(clothType.getName()).isPresent()) {
            errors.rejectValue("name", "", "Cloth type already exists");
        }
    }
}

package ru.berezhnov.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.berezhnov.models.Place;
import ru.berezhnov.repositories.PlaceRepository;

@Component
public class PlaceValidator implements Validator {

    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceValidator(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Place.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Place place = (Place) target;
        if (placeRepository.findByName(place.getName()).isPresent())
            errors.rejectValue("name", "", "Place already exists");
    }
}

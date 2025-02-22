package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.PlaceDTO;
import ru.berezhnov.models.Place;
import ru.berezhnov.services.PlaceService;
import ru.berezhnov.util.PlaceValidator;
import ru.berezhnov.util.AppException;

import java.util.List;

@Controller
@RequestMapping("/api/place")
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceValidator placeValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public PlaceController(final PlaceService placeService, PlaceValidator placeValidator,
                           ModelMapper modelMapper) {
        this.placeService = placeService;
        this.placeValidator = placeValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlaceNames() {
        return ResponseEntity.ok(placeService.findAll().stream()
                .map(this::convertPlaceToPlaceDTO).toList());
    }

    @PostMapping
    public ResponseEntity<?> addPlace(@RequestBody PlaceDTO placeDTO) {
        Place newPlace = convertPlaceDTOToPlace(placeDTO);
        Errors errors = new BeanPropertyBindingResult(newPlace, "place");
        placeValidator.validate(newPlace, errors);
        if (errors.hasErrors())
            throw new AppException("Place already exists");
        placeService.save(newPlace);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePlace(@RequestBody PlaceDTO placeDTO) {
        String placeName = placeDTO.getName();
        if (placeService.findByName(placeName).isPresent()) {
            placeService.delete(placeName);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    private PlaceDTO convertPlaceToPlaceDTO(Place place) {
        return modelMapper.map(place, PlaceDTO.class);
    }

    private Place convertPlaceDTOToPlace(PlaceDTO placeDTO) {
        return modelMapper.map(placeDTO, Place.class);
    }
}

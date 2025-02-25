package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.PlaceDTO;
import ru.berezhnov.models.Place;
import ru.berezhnov.services.PlaceService;
import ru.berezhnov.util.EmailExtractor;
import ru.berezhnov.util.PlaceValidator;
import ru.berezhnov.util.AppException;

import java.util.List;

@Controller
@RequestMapping("/api/places")
public class PlacesController {

    private final PlaceService placeService;
    private final PlaceValidator placeValidator;
    private final ModelMapper modelMapper;
    private final EmailExtractor emailExtractor;

    @Autowired
    public PlacesController(final PlaceService placeService, PlaceValidator placeValidator,
                            ModelMapper modelMapper, EmailExtractor emailExtractor) {
        this.placeService = placeService;
        this.placeValidator = placeValidator;
        this.modelMapper = modelMapper;
        this.emailExtractor = emailExtractor;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getClothTypes(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(placeService.findAllByUserEmail(emailExtractor.getUserFromHeader(authHeader))
                .stream().map(this::convertPlaceToPlaceDTO).toList());
    }

    @PostMapping
    public ResponseEntity<?> saveClothType(@RequestBody PlaceDTO placeDTO,
                                           @RequestHeader("Authorization") String authHeader) {
        Place place = convertPlaceDTOToPlace(placeDTO);
        Errors errors = new BeanPropertyBindingResult(place, "place");
        placeValidator.validate(place, errors);
        if (errors.hasErrors()) {
            throw new AppException("Place already exists");
        }
        placeService.save(emailExtractor.getUserFromHeader(authHeader), place);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> updateClothType(@RequestBody PlaceDTO placeDTO,
                                             @RequestHeader("Authorization") String authHeader) {
        placeService.update(emailExtractor.getUserFromHeader(authHeader),
                convertPlaceDTOToPlace(placeDTO));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClothType(@PathVariable("id") int id,
                                             @RequestHeader("Authorization") String authHeader) {
        placeService.deleteById(emailExtractor.getUserFromHeader(authHeader), id);
        return ResponseEntity.ok().build();
    }

    private PlaceDTO convertPlaceToPlaceDTO(Place place) {
        return modelMapper.map(place, PlaceDTO.class);
    }

    private Place convertPlaceDTOToPlace(PlaceDTO placeDTO) {
        return modelMapper.map(placeDTO, Place.class);
    }
}

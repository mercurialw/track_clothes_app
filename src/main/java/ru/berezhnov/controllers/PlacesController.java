package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.PlaceDTO;
import ru.berezhnov.models.Place;
import ru.berezhnov.services.PlaceService;
import ru.berezhnov.util.EmailExtractor;
import ru.berezhnov.util.AppException;

import java.util.List;

@Controller
@RequestMapping("/api/places")
public class PlacesController {

    private final PlaceService placeService;
    private final ModelMapper modelMapper;
    private final EmailExtractor emailExtractor;

    @Autowired
    public PlacesController(final PlaceService placeService, ModelMapper modelMapper,
                            EmailExtractor emailExtractor) {
        this.placeService = placeService;
        this.modelMapper = modelMapper;
        this.emailExtractor = emailExtractor;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlaces(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(placeService.findAllByUserEmail(emailExtractor.getUserFromHeader(authHeader))
                .stream().map(this::convertPlaceToPlaceDTO).toList());
    }

    @PostMapping
    public ResponseEntity<?> savePlace(@RequestBody PlaceDTO placeDTO,
                                       @RequestHeader("Authorization") String authHeader) {
        placeService.save(emailExtractor.getUserFromHeader(authHeader),
                convertPlaceDTOToPlace(placeDTO));
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> updatePlace(@RequestBody PlaceDTO placeDTO,
                                         @RequestHeader("Authorization") String authHeader) {
        if (placeDTO.getId() == null)
            throw new AppException("Place id is required");
        placeService.update(emailExtractor.getUserFromHeader(authHeader),
                convertPlaceDTOToPlace(placeDTO));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable("id") int id,
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

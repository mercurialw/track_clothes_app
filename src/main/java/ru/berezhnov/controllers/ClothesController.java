package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.ClothAddRequest;
import ru.berezhnov.models.Cloth;
import ru.berezhnov.dto.ClothDTO;
import ru.berezhnov.services.ClothService;
import ru.berezhnov.util.EmailExtractor;

import java.util.List;

@RestController
@RequestMapping("/api/clothes")
public class ClothesController {

    private final ModelMapper modelMapper;
    private final ClothService clothService;
    private final EmailExtractor emailExtractor;

    @Autowired
    public ClothesController(ModelMapper modelMapper, ClothService clothService, EmailExtractor emailExtractor) {
        this.modelMapper = modelMapper;
        this.clothService = clothService;
        this.emailExtractor = emailExtractor;
    }

    @GetMapping
    public ResponseEntity<List<ClothDTO>> getAllCloths(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(clothService.findAllByUserEmail(emailExtractor.getUserFromHeader(authHeader)).stream()
                .map(this::convertClothToClothDTO).toList());
    }

    @PostMapping
    public ResponseEntity<?> addClothes(@RequestBody ClothAddRequest clothAddRequest,
                                        @RequestHeader("Authorization") String authHeader) {
        clothService.save(emailExtractor.getUserFromHeader(authHeader), clothAddRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> updateClothes(@RequestBody ClothDTO clothDTO,
                                           @RequestHeader("Authorization") String authHeader) {
        clothService.update(emailExtractor.getUserFromHeader(authHeader), convertClothDTOToCloth(clothDTO));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClothes(@PathVariable("id") int id,
                                           @RequestHeader("Authorization") String authHeader) {
        clothService.delete(emailExtractor.getUserFromHeader(authHeader), id);
        return ResponseEntity.ok().build();
    }

    private ClothDTO convertClothToClothDTO(Cloth cloth) {
        return modelMapper.map(cloth, ClothDTO.class);
    }

    private Cloth convertClothDTOToCloth(ClothDTO clothDTO) {
        return modelMapper.map(clothDTO, Cloth.class);
    }
}

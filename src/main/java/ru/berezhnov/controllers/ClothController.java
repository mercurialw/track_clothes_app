package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.UsersClothRequest;
import ru.berezhnov.models.Cloth;
import ru.berezhnov.dto.ClothDTO;
import ru.berezhnov.services.ClothService;

import java.util.List;

@RestController
@RequestMapping("/api/cloth")
public class ClothController {

    private final ModelMapper modelMapper;
    private final ClothService clothService;

    @Autowired
    public ClothController(ModelMapper modelMapper, ClothService clothService) {
        this.modelMapper = modelMapper;
        this.clothService = clothService;
    }

    @GetMapping
    public ResponseEntity<List<ClothDTO>> getAllCloths() {
        return ResponseEntity.ok(clothService.findAll().stream()
                .map(this::convertClothToClothDTO).toList());
    }

    @PostMapping // todo
    public ResponseEntity<?> addClothToUser(@RequestBody UsersClothRequest ucr) {
        return null;
    }

    private ClothDTO convertClothToClothDTO(Cloth cloth) {
        return modelMapper.map(cloth, ClothDTO.class);
    }
}

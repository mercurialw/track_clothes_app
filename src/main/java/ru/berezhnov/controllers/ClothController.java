package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    private ClothDTO convertClothToClothDTO(Cloth cloth) {
        return modelMapper.map(cloth, ClothDTO.class);
    }
}

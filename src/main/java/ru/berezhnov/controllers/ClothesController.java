package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.UserDTO;
import ru.berezhnov.models.Cloth;
import ru.berezhnov.dto.ClothDTO;
import ru.berezhnov.models.User;
import ru.berezhnov.services.ClothService;

import java.util.List;

@RestController
@RequestMapping("/api/clothes")
public class ClothesController {

    private final ModelMapper modelMapper;
    private final ClothService clothService;

    @Autowired
    public ClothesController(ModelMapper modelMapper, ClothService clothService) {
        this.modelMapper = modelMapper;
        this.clothService = clothService;
    }

    @GetMapping
    public ResponseEntity<List<ClothDTO>> getAllCloths() {
        return ResponseEntity.ok(clothService.findAll().stream()
                .map(this::convertClothToClothDTO).toList());
    }

    @PostMapping
    public ResponseEntity<?> addClothes(@RequestBody List<ClothDTO> clothDTOs) {
        clothDTOs.forEach(clothDTO -> clothService.save(convertClothDTOToCloth(clothDTO)));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateClothes(@PathVariable("id") int id,
                                           @RequestBody ClothDTO clothDTO) {
        clothService.update(id, convertClothDTOToCloth(clothDTO));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClothes(@PathVariable("id") int id) {
        clothService.delete(id);
        return ResponseEntity.ok().build();
    }

    private ClothDTO convertClothToClothDTO(Cloth cloth) {
        ClothDTO clothDTO = modelMapper.map(cloth, ClothDTO.class);
        clothDTO.setOwner(convertUserToUserDTO(cloth.getOwner()));
        return clothDTO;
    }

    private Cloth convertClothDTOToCloth(ClothDTO clothDTO) {
        return modelMapper.map(clothDTO, Cloth.class);
    }

    private UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}

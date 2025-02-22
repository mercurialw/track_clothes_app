package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.ClothTypeDTO;
import ru.berezhnov.models.ClothType;
import ru.berezhnov.services.ClothTypeService;
import ru.berezhnov.util.ClothTypeValidator;
import ru.berezhnov.util.AppException;

import java.util.List;

@RestController
@RequestMapping("/api/cloth_type")
public class ClothTypeController {

    private final ClothTypeService clothTypeService;
    private final ClothTypeValidator clothTypeValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public ClothTypeController(final ClothTypeService clothTypeService, ClothTypeValidator clothTypeValidator, ModelMapper modelMapper) {
        this.clothTypeService = clothTypeService;
        this.clothTypeValidator = clothTypeValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<ClothTypeDTO>> getClothTypes() {
        return ResponseEntity.ok(clothTypeService.findAll().stream()
                .map(this::convertClothTypeToClothTypeDTO).toList());
    }

    @PostMapping
    public ResponseEntity<?> saveClothType(@RequestBody ClothTypeDTO clothTypeDTO) {
        ClothType clothType = convertClothTypeDTOToClothType(clothTypeDTO);
        Errors errors = new BeanPropertyBindingResult(clothType, "clothType");
        clothTypeValidator.validate(clothType, errors);
        if (errors.hasErrors()) {
            throw new AppException("Cloth type already exists");
        }
        clothTypeService.save(clothType);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteClothType(@RequestBody ClothTypeDTO clothTypeDTO) {
        String clothTypeName = clothTypeDTO.getName();
        if (clothTypeService.findByName(clothTypeName).isPresent()) {
            clothTypeService.delete(clothTypeName);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    private ClothTypeDTO convertClothTypeToClothTypeDTO(ClothType clothType) {
        return modelMapper.map(clothType, ClothTypeDTO.class);
    }

    private ClothType convertClothTypeDTOToClothType(ClothTypeDTO clothTypeDTO) {
        return modelMapper.map(clothTypeDTO, ClothType.class);
    }
}

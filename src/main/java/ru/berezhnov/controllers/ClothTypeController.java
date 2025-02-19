package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.cloth.type.ClothType;
import ru.berezhnov.cloth.type.ClothTypeDTO;
import ru.berezhnov.cloth.type.ClothTypeService;
import ru.berezhnov.cloth.type.ClothTypeValidator;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cloth_type")
public class ClothTypeController {

    private final ClothTypeService clothTypeService;
    private final ModelMapper modelMapper;
    private final ClothTypeValidator clothTypeValidator;

    @Autowired
    public ClothTypeController(final ClothTypeService clothTypeService, ModelMapper modelMapper, ClothTypeValidator clothTypeValidator) {
        this.clothTypeService = clothTypeService;
        this.modelMapper = modelMapper;
        this.clothTypeValidator = clothTypeValidator;
    }

    @GetMapping
    public ResponseEntity<List<ClothTypeDTO>> getClothTypes() {
        return ResponseEntity.ok(clothTypeService.findAll().stream()
                .map(this::convertClothTypeToClothTypeDTO).toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<ClothTypeDTO> getClothTypeById(@PathVariable final long id) {
        Optional<ClothType> type = clothTypeService.findOne(id);
        if (type.isPresent())
            return ResponseEntity.ok(convertClothTypeToClothTypeDTO(type.get()));
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> saveClothType(@RequestBody ClothTypeDTO clothTypeDTO) {
        ClothType clothType = convertClothTypeDTOToClothType(clothTypeDTO);
        Errors errors = new BeanPropertyBindingResult(clothType, "clothType");
        clothTypeValidator.validate(clothType, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
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

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteClothType(@PathVariable final long id) {
        Optional<ClothType> type = clothTypeService.findOne(id);
        if (type.isPresent()) {
            clothTypeService.delete(id);
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

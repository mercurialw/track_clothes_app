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
import ru.berezhnov.util.EmailExtractor;

import java.util.List;

@RestController
@RequestMapping("/api/cloth_types")
public class ClothTypesController {

    private final ClothTypeService clothTypeService;
    private final ClothTypeValidator clothTypeValidator;
    private final ModelMapper modelMapper;
    private final EmailExtractor emailExtractor;

    @Autowired
    public ClothTypesController(final ClothTypeService clothTypeService, ClothTypeValidator clothTypeValidator, ModelMapper modelMapper, EmailExtractor emailExtractor) {
        this.clothTypeService = clothTypeService;
        this.clothTypeValidator = clothTypeValidator;
        this.modelMapper = modelMapper;
        this.emailExtractor = emailExtractor;
    }

    @GetMapping
    public ResponseEntity<List<ClothTypeDTO>> getClothTypes(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(clothTypeService.findAllByUserEmail(emailExtractor.getUserFromHeader(authHeader))
                .stream().map(this::convertClothTypeToClothTypeDTO).toList());
    }

    @PostMapping
    public ResponseEntity<?> saveClothType(@RequestBody ClothTypeDTO clothTypeDTO,
                                           @RequestHeader("Authorization") String authHeader) {
        ClothType clothType = convertClothTypeDTOToClothType(clothTypeDTO);
        Errors errors = new BeanPropertyBindingResult(clothType, "clothType");
        clothTypeValidator.validate(clothType, errors);
        if (errors.hasErrors()) {
            throw new AppException("Cloth type already exists");
        }

        clothTypeService.save(emailExtractor.getUserFromHeader(authHeader), clothType);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteClothType(@RequestBody ClothTypeDTO clothTypeDTO) {
        String clothTypeName = clothTypeDTO.getName();
        if (clothTypeService.findByName(clothTypeName).isPresent()) {
            clothTypeService.delete(clothTypeName);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        throw new AppException("Cloth type does not exist");
    }

    private ClothTypeDTO convertClothTypeToClothTypeDTO(ClothType clothType) {
        return modelMapper.map(clothType, ClothTypeDTO.class);
    }

    private ClothType convertClothTypeDTOToClothType(ClothTypeDTO clothTypeDTO) {
        return modelMapper.map(clothTypeDTO, ClothType.class);
    }
}

package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.config.JwtService;
import ru.berezhnov.dto.ClothDTO;
import ru.berezhnov.dto.UserDTO;
import ru.berezhnov.models.Cloth;
import ru.berezhnov.models.ClothType;
import ru.berezhnov.models.Place;
import ru.berezhnov.models.User;
import ru.berezhnov.services.UserService;
import ru.berezhnov.util.AppException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final JwtService jwtService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(JwtService jwtService, UserService userService, ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        Optional<User> user = getUserFromHeader(authHeader);
        return ResponseEntity.ok(getUserEmail(user.orElseThrow(() ->
                new AppException("User not found"))));
    }

    @GetMapping("/clothes")
    public ResponseEntity<List<ClothDTO>> getUsersClothes(@RequestHeader("Authorization") String authHeader) {
        Optional<User> user = getUserFromHeader(authHeader);
        return ResponseEntity.ok(user.get().getClothes().stream()
                .map(this::convertClothToClothDTO).toList());
    }

    // unnecessary method
    @PostMapping("/clothes")
    public ResponseEntity<?> saveUserClothes(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody List<ClothDTO> clothDTOs) {
        if (clothDTOs.isEmpty())
            throw new AppException("There are no clothes");
        userService.save(getUserFromHeader(authHeader).orElseThrow(() -> new AppException("User not found")),
                clothDTOs.stream().map(this::convertClothDTOToCloth).toList());
        return ResponseEntity.ok().build();
    }

    private Optional<User> getUserFromHeader(String authHeader) {
        return userService.findByEmail(jwtService
                .extractUsername(authHeader.substring("Bearer ".length())));
    }

    private ClothDTO convertClothToClothDTO(Cloth cloth) {
        return modelMapper.map(cloth, ClothDTO.class);
    }

    private Cloth convertClothDTOToCloth(ClothDTO clothDTO) {
        Cloth result = modelMapper.map(clothDTO, Cloth.class);
        result.setType(new ClothType(clothDTO.getType().getName()));
        result.setPlace(new Place(clothDTO.getPlace().getName()));
        return result;
    }

    private UserDTO getUserEmail(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}

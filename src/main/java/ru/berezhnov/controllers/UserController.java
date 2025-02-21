package ru.berezhnov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.config.JwtService;
import ru.berezhnov.dto.ClothDTO;
import ru.berezhnov.models.Cloth;
import ru.berezhnov.models.ClothType;
import ru.berezhnov.models.User;
import ru.berezhnov.services.UserService;
import ru.berezhnov.util.TrackClothesAppException;

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
    public ResponseEntity<String> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        Optional<User> user = getUserFromHeader(authHeader);
        return ResponseEntity.ok(user.get().getEmail());
    }

    @GetMapping("/clothes")
    public ResponseEntity<List<ClothDTO>> getUsersClothes(@RequestHeader("Authorization") String authHeader) {
        Optional<User> user = getUserFromHeader(authHeader);
        return ResponseEntity.ok(user.get().getClothes().stream()
                .map(this::convertClothToClothDTO).toList());
    }

    @PostMapping("/clothes")
    public ResponseEntity<?> saveUserClothes(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody List<ClothDTO> clothDTOs) {
        if (clothDTOs.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        userService.save(getUserFromHeader(authHeader).orElseThrow(() -> new TrackClothesAppException("User not found")),
                clothDTOs.stream().map(this::convertClothDTOToCloth).toList());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/clothes/update_name")
    public ResponseEntity<?> updateUserClothName(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody List<String> names) {
        if (names.size() < 2)
            return ResponseEntity.badRequest().build();
        if (names.get(0).equals(names.get(1)))
            throw new TrackClothesAppException("Names are the same");
        userService.update(getUserFromHeader(authHeader).orElseThrow(() ->
                new TrackClothesAppException("User not found")), names.get(0), names.get(1));
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
        return result;
    }

    @ExceptionHandler
    public ResponseEntity<String> exception(TrackClothesAppException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

package ru.berezhnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berezhnov.models.Cloth;
import ru.berezhnov.models.ClothType;
import ru.berezhnov.models.Place;
import ru.berezhnov.models.User;
import ru.berezhnov.repositories.ClothRepository;
import ru.berezhnov.repositories.ClothTypeRepository;
import ru.berezhnov.repositories.PlaceRepository;
import ru.berezhnov.repositories.UserRepository;
import ru.berezhnov.util.AppException;

import java.util.List;
import java.util.Optional;

//unnecessary class
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ClothRepository clothRepository;
    private final ClothTypeRepository clothTypeRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public UserService(UserRepository userRepository, ClothRepository clothRepository, ClothTypeRepository clothTypeRepository, PlaceRepository placeRepository) {
        this.userRepository = userRepository;
        this.clothRepository = clothRepository;
        this.clothTypeRepository = clothTypeRepository;
        this.placeRepository = placeRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void save(User user, List<Cloth> list) {
        User persistedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new AppException("User not found"));
        list.forEach(clothToSave -> {
            clothToSave.setOwner(persistedUser);
            ClothType persistedClothType = clothTypeRepository.findByName(clothToSave.getType().getName())
                    .orElseThrow(() -> new AppException("Cloth type not found"));
            clothToSave.setType(persistedClothType);
            Place persistedPlace = placeRepository.findByName(clothToSave.getPlace().getName())
                    .orElseThrow(() -> new AppException("Place not found"));
            clothToSave.setPlace(persistedPlace);
            clothRepository.save(clothToSave);
        });
    }
}

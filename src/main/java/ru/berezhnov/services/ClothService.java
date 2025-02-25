package ru.berezhnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berezhnov.dto.UserDTO;
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

@Service
@Transactional(readOnly = true)
public class ClothService {

    private final ClothRepository clothRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ClothTypeRepository clothTypeRepository;

    @Autowired
    public ClothService(ClothRepository clothRepository, UserRepository userRepository, PlaceRepository placeRepository, ClothTypeRepository clothTypeRepository) {
        this.clothRepository = clothRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.clothTypeRepository = clothTypeRepository;
    }

    public List<Cloth> findAllByUserEmail(UserDTO user) {
        return clothRepository.findAllByUserEmail(user.getEmail());
    }

    @Transactional
    public void save(UserDTO user, Cloth cloth) {
        User persistedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new AppException("User not found"));
        Place persistedPlace = placeRepository.findByName(cloth.getPlace().getName())
                .orElseThrow(() -> new AppException("Place not found"));
        if (!persistedPlace.getOwner().equals(persistedUser)) {
            throw new AppException("Place is not owned by user");
        }
        ClothType persistedClothType = clothTypeRepository.findByName(cloth.getType()
                .getName()).orElseThrow(() -> new AppException("ClothType not found"));
        if (!persistedClothType.getOwner().equals(persistedUser)) {
            throw new AppException("Cloth type is not owned by user");
        }
        cloth.setPlace(persistedPlace);
        cloth.setType(persistedClothType);
        clothRepository.save(cloth);
    }

    @Transactional
    public void update(UserDTO user, Cloth cloth) {
        User persistedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() ->
                new AppException("User not found"));
        Cloth persistedCloth = clothRepository.findById(cloth.getId()).orElseThrow(() ->
                new AppException("Cloth not found"));
        if (!persistedCloth.getType().getOwner().equals(persistedUser)) {
            throw new AppException("Cloth is not owned by user");
        }
        if (cloth.getPlace() != null && cloth.getPlace().getName() != null) {
            if (!cloth.getPlace().getOwner().equals(persistedUser)) {
                throw new AppException("Place is not owned by user");
            }
            persistedCloth.setPlace(placeRepository.findByName(cloth.getPlace().getName())
                    .orElseThrow(() -> new AppException("Place not found")));
        }
        if (cloth.getType() != null && cloth.getType().getName() != null) {
            if (!cloth.getType().getOwner().equals(persistedUser)) {
                throw new AppException("Cloth type is not owned by user");
            }
            persistedCloth.setType(clothTypeRepository.findByName(cloth.getType().getName())
                    .orElseThrow(() -> new AppException("Cloth type not found")));
        }
        if (cloth.getName() != null) {
            persistedCloth.setName(cloth.getName());
        }
        if (cloth.getPhotoUrl() != null) {
            persistedCloth.setPhotoUrl(cloth.getPhotoUrl());
        }
        if (cloth.getSize() != null) {
            persistedCloth.setSize(cloth.getSize());
        }
    }

    @Transactional
    public void delete(UserDTO user, int id) {
        User persistedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() ->
                new AppException("User not found"));
        Cloth persistedCloth = clothRepository.findById(id).orElseThrow(() -> new AppException("Cloth not found"));
        if (!persistedCloth.getType().getOwner().equals(persistedUser)) {
            throw new AppException("Cloth type is not owned by user");
        }
        clothRepository.deleteById(id);
    }
}

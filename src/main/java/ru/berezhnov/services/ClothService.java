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

    public List<Cloth> findAll() {
        return clothRepository.findAll();
    }

    public Optional<Cloth> findById(int id) {
        return clothRepository.findById(id);
    }

    @Transactional
    public void save(Cloth cloth) {
        User persistedUser = userRepository.findByEmail(cloth.getOwner().getEmail())
                .orElseThrow(() -> new AppException("User not found"));
        Place persistedPlace = placeRepository.findByName(cloth.getPlace().getName())
                .orElseThrow(() -> new AppException("Place not found"));
        ClothType persistedClothType = clothTypeRepository.findByName(cloth.getType()
                .getName()).orElseThrow(() -> new AppException("ClothType not found"));
        cloth.setOwner(persistedUser);
        cloth.setPlace(persistedPlace);
        cloth.setType(persistedClothType);
        clothRepository.save(cloth);
    }

    @Transactional
    public void update(int id, Cloth proxyCloth) {
        Cloth persistedCloth = clothRepository.findById(id).orElseThrow(() ->
                new AppException("Cloth not found"));
        if (proxyCloth.getName() != null) {
            persistedCloth.setName(proxyCloth.getName());
        }
        if (proxyCloth.getPhotoUrl() != null) {
            persistedCloth.setPhotoUrl(proxyCloth.getPhotoUrl());
        }
        if (proxyCloth.getSize() != null) {
            persistedCloth.setSize(proxyCloth.getSize());
        }
        if (proxyCloth.getType() != null && proxyCloth.getType().getName() != null) {
            persistedCloth.setType(clothTypeRepository.findByName(proxyCloth.getType().getName())
                .orElseThrow(() -> new AppException("ClothType not found")));
        }
        if (proxyCloth.getPlace() != null && proxyCloth.getPlace().getName() != null) {
            persistedCloth.setPlace(placeRepository.findByName(proxyCloth.getPlace().getName())
                .orElseThrow(() -> new AppException("Place not found")));
        }
        if (proxyCloth.getOwner() != null && proxyCloth.getOwner().getEmail() != null) {
            persistedCloth.setOwner(userRepository.findByEmail(proxyCloth.getOwner().getEmail())
                .orElseThrow(() -> new AppException("Owner not found")));
        }
    }

    @Transactional
    public void delete(int id) {
        clothRepository.findById(id).orElseThrow(() -> new AppException("Cloth not found"));
        clothRepository.deleteById(id);
    }
}

package ru.berezhnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berezhnov.dto.UserDTO;
import ru.berezhnov.models.Place;
import ru.berezhnov.models.User;
import ru.berezhnov.repositories.PlaceRepository;
import ru.berezhnov.repositories.UserRepository;
import ru.berezhnov.util.AppException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    public List<Place> findAllByUserEmail(UserDTO user) {
        return placeRepository.findAllByUserEmail(user.getEmail());
    }

    @Transactional
    public void save(UserDTO user, Place placeToSave) {
        placeToSave.setId(0);
        User persistedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(()
                -> new AppException("User not found"));
        if (persistedUser.getPlaces().stream().anyMatch(p -> placeToSave.getName().equals(p.getName())))
            throw new AppException("Place already exists");
        placeToSave.setOwner(persistedUser);
        placeRepository.save(placeToSave);
    }

    @Transactional
    public void update(UserDTO user, Place placeToUpdate) {
        User persistedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(()
                -> new AppException("User not found"));
        if (persistedUser.getPlaces().stream().anyMatch(p -> placeToUpdate.getName().equals(p.getName())))
            throw new AppException("Place already exists");
        if (persistedUser.getPlaces().stream().anyMatch(p -> p.getId() == placeToUpdate.getId())) {
            placeRepository.findById(placeToUpdate.getId()).orElseThrow(()
                    -> new AppException("Place not found")).setName(placeToUpdate.getName());
        } else
            throw new AppException("Place is not owned by user");
    }

    @Transactional
    public void deleteById(UserDTO user, int id) {
        if (userRepository.findByEmail(user.getEmail()).orElseThrow(()
                        -> new AppException("User not found")).getPlaces()
                .stream().anyMatch(t -> t.getId() == id)) {
            placeRepository.deleteById(id);
        } else
            throw new AppException("Place is not owned by user");
    }
}

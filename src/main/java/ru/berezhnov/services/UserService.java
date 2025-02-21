package ru.berezhnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berezhnov.models.Cloth;
import ru.berezhnov.models.ClothType;
import ru.berezhnov.models.User;
import ru.berezhnov.repositories.ClothRepository;
import ru.berezhnov.repositories.ClothTypeRepository;
import ru.berezhnov.repositories.UserRepository;
import ru.berezhnov.util.TrackClothesAppException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ClothRepository clothRepository;
    private final ClothTypeRepository clothTypeRepository;

    @Autowired
    public UserService(UserRepository userRepository, ClothRepository clothRepository, ClothTypeRepository clothTypeRepository) {
        this.userRepository = userRepository;
        this.clothRepository = clothRepository;
        this.clothTypeRepository = clothTypeRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void save(User user, List<Cloth> list) {
        User persistedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new TrackClothesAppException("User not found"));
        list.forEach(cloth -> {
            cloth.setOwner(persistedUser);
            ClothType persistedClothType = clothTypeRepository.findByName(cloth.getType().getName())
                    .orElseThrow(() -> new TrackClothesAppException("Cloth type not found"));
            cloth.setType(persistedClothType);
            clothRepository.save(cloth);
        });

    }

    @Transactional
    public void update(User user, String oldName, String newName) {
        clothRepository.findByNameAndOwnerName(oldName, user.getEmail())
                .orElseThrow(() -> new TrackClothesAppException("Cloth not found"))
                .setName(newName);
    }
}

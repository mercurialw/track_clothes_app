package ru.berezhnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berezhnov.dto.UserDTO;
import ru.berezhnov.models.ClothType;
import ru.berezhnov.repositories.ClothTypeRepository;
import ru.berezhnov.repositories.UserRepository;
import ru.berezhnov.util.AppException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClothTypeService {

    private final ClothTypeRepository clothTypeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClothTypeService(ClothTypeRepository clothTypeRepository, UserRepository userRepository) {
        this.clothTypeRepository = clothTypeRepository;
        this.userRepository = userRepository;
    }

    public List<ClothType> findAllByUserEmail(UserDTO user) {
        return clothTypeRepository.findAllByUserEmail(user.getEmail());
    }

    public Optional<ClothType> findByName(String name) {
        return clothTypeRepository.findByName(name);
    }

    @Transactional
    public void save(UserDTO user, ClothType clothType) {
        clothType.setId(0);
        clothType.setOwner(userRepository.findByEmail(user.getEmail()).orElseThrow(()
                -> new AppException("User not found")));
        clothTypeRepository.save(clothType);
    }

    @Transactional
    public void deleteById(UserDTO user, int id) {
        if (userRepository.findByEmail(user.getEmail()).orElseThrow(()
                -> new AppException("User not found")).getClothTypes()
                .stream().anyMatch(t -> t.getId() == id)) {
            clothTypeRepository.deleteById(id);
        } else
            throw new AppException("Cloth type is not owned by user");
    }

    @Transactional
    public void update(UserDTO user, ClothType clothType) {
        if (userRepository.findByEmail(user.getEmail()).orElseThrow(()
                        -> new AppException("User not found")).getClothTypes()
                .stream().anyMatch(t -> t.getId() == clothType.getId())) {
            clothTypeRepository.findById(clothType.getId()).orElseThrow(()
                    -> new AppException("Cloth type not found")).setName(clothType.getName());
        } else
            throw new AppException("Cloth type is not owned by user");
    }
}

package ru.berezhnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berezhnov.models.ClothType;
import ru.berezhnov.repositories.ClothTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClothTypeService {

    private final ClothTypeRepository clothTypeRepository;

    @Autowired
    public ClothTypeService(ClothTypeRepository clothTypeRepository) {
        this.clothTypeRepository = clothTypeRepository;
    }

    public List<ClothType> findAll() {
        return clothTypeRepository.findAll();
    }

    public Optional<ClothType> findByName(String name) {
        return clothTypeRepository.findByName(name);
    }

    @Transactional
    public void save(ClothType clothType) {
        clothTypeRepository.save(clothType);
    }

    @Transactional
    public void delete(String name) {
        clothTypeRepository.deleteByName(name);
    }
}

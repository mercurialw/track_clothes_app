package ru.berezhnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berezhnov.models.Cloth;
import ru.berezhnov.repositories.ClothRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClothService {

    private final ClothRepository clothRepository;

    @Autowired
    public ClothService(ClothRepository clothRepository) {
        this.clothRepository = clothRepository;
    }

    public List<Cloth> findAll() {
        return clothRepository.findAll();
    }

    public Optional<Cloth> findById(int id) {
        return clothRepository.findById(id);
    }
}

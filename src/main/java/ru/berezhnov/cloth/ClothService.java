package ru.berezhnov.cloth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClothService {

    private final ClothRepository clothRepository;

    @Autowired
    public ClothService(ClothRepository clothRepository) {
        this.clothRepository = clothRepository;
    }
}

package ru.berezhnov.cloth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothService {

    private final ClothRepository clothRepository;

}

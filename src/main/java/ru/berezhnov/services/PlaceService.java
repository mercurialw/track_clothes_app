package ru.berezhnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berezhnov.models.Place;
import ru.berezhnov.repositories.PlaceRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    public Optional<Place> findById(int id) {
        return placeRepository.findById(id);
    }

    public Optional<Place> findByName(String name) {
        return placeRepository.findByName(name);
    }

    @Transactional
    public void save(Place place) {
        placeRepository.save(place);
    }

    @Transactional
    public void delete(String placeName) {
        placeRepository.deleteByName(placeName);
    }
}

package ru.berezhnov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.berezhnov.models.Place;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    void deleteByName(String name);
    Optional<Place> findByName(String name);
}

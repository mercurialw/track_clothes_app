package ru.berezhnov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.berezhnov.models.Place;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    @Query("from Place p inner join p.owner u where u.email = :ownerEmail") // select distinct ct
    List<Place> findAllByUserEmail(@Param("ownerEmail") String ownerEmail);
    Optional<Place> findByName(String name);
}

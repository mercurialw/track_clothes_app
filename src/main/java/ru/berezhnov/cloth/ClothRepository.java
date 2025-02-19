package ru.berezhnov.cloth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClothRepository extends JpaRepository<Cloth, Integer> {
    Optional<Cloth> findByName(String name);
}

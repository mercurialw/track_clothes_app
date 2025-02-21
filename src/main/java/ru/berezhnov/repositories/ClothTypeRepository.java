package ru.berezhnov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.berezhnov.models.ClothType;

import java.util.Optional;

@Repository
public interface ClothTypeRepository extends JpaRepository<ClothType, Long> {
    Optional<ClothType> findByName(String name);
    void deleteByName(String name);
}

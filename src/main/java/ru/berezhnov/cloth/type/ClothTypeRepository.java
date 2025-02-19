package ru.berezhnov.cloth.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClothTypeRepository extends JpaRepository<ClothType, Long> {
    Optional<ClothType> findByName(String name);
    void deleteByName(String name);
}

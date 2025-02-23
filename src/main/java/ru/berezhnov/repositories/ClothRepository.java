package ru.berezhnov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.berezhnov.models.Cloth;

import java.util.Optional;

@Repository
public interface ClothRepository extends JpaRepository<Cloth, Integer> {
    Optional<Cloth> findByName(String name);
    @Query("from Cloth c where c.name = :clothName and c.owner.email = :ownerEmail")
    Optional<Cloth> findByNameAndOwnerName (@Param("clothName") String clothName, @Param("ownerEmail") String ownerEmail);
}

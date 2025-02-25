package ru.berezhnov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.berezhnov.models.ClothType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClothTypeRepository extends JpaRepository<ClothType, Integer> {
    Optional<ClothType> findByName(String name);
    @Query("from ClothType ct inner join ct.owner u where u.email = :ownerEmail") // select distinct ct
    List<ClothType> findAllByUserEmail(@Param("ownerEmail") String ownerEmail);
}

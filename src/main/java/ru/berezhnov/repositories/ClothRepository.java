package ru.berezhnov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.berezhnov.models.Cloth;

import java.util.List;

@Repository
public interface ClothRepository extends JpaRepository<Cloth, Integer> {
    @Query("from Cloth c inner join c.type ct inner join ct.owner u where u.email = :email")
    List<Cloth> findAllByUserEmail(@Param("email") String email);
}

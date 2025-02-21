package ru.berezhnov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.berezhnov.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}

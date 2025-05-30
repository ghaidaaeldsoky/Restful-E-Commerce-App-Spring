package iti.jets.misk.repositories;

import iti.jets.misk.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll(); // It should not return DTO

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);
}

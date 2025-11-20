package cuidei_api.fiap.cuidei_api.repositories;

import cuidei_api.fiap.cuidei_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "SELECT * FROM users WHERE username = ?", nativeQuery = true)
    Optional<User> findByUsername(String username);
}

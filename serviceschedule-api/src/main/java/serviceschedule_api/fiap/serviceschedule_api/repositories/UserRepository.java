package serviceschedule_api.fiap.serviceschedule_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
    Optional<User> findByCpf(String cpf);
}
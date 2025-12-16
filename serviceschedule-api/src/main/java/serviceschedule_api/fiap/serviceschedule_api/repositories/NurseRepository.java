package serviceschedule_api.fiap.serviceschedule_api.repositories;

import serviceschedule_api.fiap.serviceschedule_api.entities.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
}

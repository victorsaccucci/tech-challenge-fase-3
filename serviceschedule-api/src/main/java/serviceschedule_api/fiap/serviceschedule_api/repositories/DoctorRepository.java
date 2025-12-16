package serviceschedule_api.fiap.serviceschedule_api.repositories;

import serviceschedule_api.fiap.serviceschedule_api.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}

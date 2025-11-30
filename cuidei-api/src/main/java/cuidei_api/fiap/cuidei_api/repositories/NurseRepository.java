package cuidei_api.fiap.cuidei_api.repositories;

import cuidei_api.fiap.cuidei_api.entities.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
}

package cuidei_api.fiap.cuidei_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import cuidei_api.fiap.cuidei_api.entities.Nurse;

public interface NurseRepository extends JpaRepository<Nurse, Long> {

}

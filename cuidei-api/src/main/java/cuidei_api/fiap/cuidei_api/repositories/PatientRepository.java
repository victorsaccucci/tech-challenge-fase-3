package cuidei_api.fiap.cuidei_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import cuidei_api.fiap.cuidei_api.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}

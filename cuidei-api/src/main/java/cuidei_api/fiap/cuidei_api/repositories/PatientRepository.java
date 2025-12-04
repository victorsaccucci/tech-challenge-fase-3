package cuidei_api.fiap.cuidei_api.repositories;

import cuidei_api.fiap.cuidei_api.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByCpf(String cpf);

    Optional<Patient> findByEmail(String email);
}


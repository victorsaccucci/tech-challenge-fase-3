package cuidei_api.fiap.cuidei_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import cuidei_api.fiap.cuidei_api.entities.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long>  {

}

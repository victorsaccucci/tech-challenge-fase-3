package cuidei_api.fiap.cuidei_api.repositories;

import cuidei_api.fiap.cuidei_api.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}


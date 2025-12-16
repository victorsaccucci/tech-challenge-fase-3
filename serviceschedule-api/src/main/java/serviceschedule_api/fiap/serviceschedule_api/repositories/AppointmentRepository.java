package serviceschedule_api.fiap.serviceschedule_api.repositories;

import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient_Id(Long patientId);

    List<Appointment> findByDoctor_Id(Long doctorId);

    List<Appointment> findByNurse_Id(Long nurseId);

    @Query("""
                SELECT a FROM Appointment a
                LEFT JOIN FETCH a.doctor
                LEFT JOIN FETCH a.nurse
                LEFT JOIN FETCH a.patient
                WHERE a.id = :id
            """)
    Optional<Appointment> findByIdWithRelations(@Param("id") Long id);
}


package serviceschedule_api.fiap.serviceschedule_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(User patient);
    List<Appointment> findByDoctor(User doctor);
    List<Appointment> findByStatus(AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN ?1 AND ?2")
    List<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor = ?1 AND a.appointmentDate BETWEEN ?2 AND ?3")
    List<Appointment> findByDoctorAndDateRange(User doctor, LocalDateTime start, LocalDateTime end);
    
    List<Appointment> findByAppointmentDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, AppointmentStatus status);
}
package serviceschedule_api.fiap.serviceschedule_api.services;

import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import serviceschedule_api.fiap.serviceschedule_api.repositories.AppointmentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DoctorService {

    private final AppointmentRepository appointmentRepository;

    public DoctorService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findAppointments(Long doctorId) {
        return appointmentRepository.findByDoctor_Id(doctorId);
    }

    @Transactional
    public Appointment updateAppointment(Long id, Appointment updatedData) {
        return appointmentRepository.findByIdWithRelations(id).orElse(null);
    }
}

package cuidei_api.fiap.cuidei_api.services;

import cuidei_api.fiap.cuidei_api.entities.Appointment;
import cuidei_api.fiap.cuidei_api.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment update(Long id, Appointment updated) {
        Appointment existing = findById(id);

        existing.setDoctor(updated.getDoctor());
        existing.setPatient(updated.getPatient());
        existing.setDateTime(updated.getDateTime());
        existing.setReason(updated.getReason());

        return appointmentRepository.save(existing);
    }


}

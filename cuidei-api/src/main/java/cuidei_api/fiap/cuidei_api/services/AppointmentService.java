package cuidei_api.fiap.cuidei_api.services;

import cuidei_api.fiap.cuidei_api.entities.Appointment;
import cuidei_api.fiap.cuidei_api.entities.Patient;
import cuidei_api.fiap.cuidei_api.repositories.AppointmentRepository;
import cuidei_api.fiap.cuidei_api.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public List<Appointment> findByPatient_Id(Long patientId) {
        return appointmentRepository.findByPatient_Id(patientId);
    }

    public Appointment save(Appointment appt) {
        return appointmentRepository.save(appt);
    }

    public Appointment update(Long id, Appointment updated) {
        Appointment original = findById(id);

        original.setDateTime(updated.getDateTime());
        original.setDoctor(updated.getDoctor());
        original.setPatient(updated.getPatient());

        return appointmentRepository.save(original);
    }
}

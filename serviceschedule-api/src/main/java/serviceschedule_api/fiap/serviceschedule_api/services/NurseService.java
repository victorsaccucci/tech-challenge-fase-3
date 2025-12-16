package serviceschedule_api.fiap.serviceschedule_api.services;

import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import serviceschedule_api.fiap.serviceschedule_api.repositories.AppointmentRepository;
import serviceschedule_api.fiap.serviceschedule_api.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NurseService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public NurseService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public List<Appointment> findAppointments(Long nurseId) {
        return appointmentRepository.findByNurse_Id(nurseId);
    }

    public Appointment registerAppointment(Appointment appointment) {

        if (appointment.getPatient().getId() == null) {

            patientRepository.findByCpf(appointment.getPatient().getCpf())
                    .ifPresentOrElse(
                            appointment::setPatient,
                            () -> {
                                var saved = patientRepository.save(appointment.getPatient());
                                appointment.setPatient(saved);
                            }
                    );
        }

        return appointmentRepository.save(appointment);
    }
}

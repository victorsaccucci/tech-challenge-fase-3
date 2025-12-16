package serviceschedule_api.fiap.serviceschedule_api.services;

import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import serviceschedule_api.fiap.serviceschedule_api.entities.Patient;
import serviceschedule_api.fiap.serviceschedule_api.repositories.AppointmentRepository;
import serviceschedule_api.fiap.serviceschedule_api.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getLoggedPatientAppointments(String loggedCpf) {

        Patient patient = patientRepository.findByCpf(loggedCpf)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado no banco"));

        return appointmentRepository.findByPatient_Id(patient.getId());
    }

}

package serviceschedule_api.fiap.serviceschedule_api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.AppointmentStatus;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;
import serviceschedule_api.fiap.serviceschedule_api.exceptions.BusinessException;
import serviceschedule_api.fiap.serviceschedule_api.exceptions.ResourceNotFoundException;
import serviceschedule_api.fiap.serviceschedule_api.repositories.AppointmentRepository;
import serviceschedule_api.fiap.serviceschedule_api.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    
    public AppointmentService(AppointmentRepository appointmentRepository, 
                            UserRepository userRepository, 
                            RabbitTemplate rabbitTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public Appointment createAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDate, String notes) {
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data do agendamento deve ser no futuro");
        }
        
        User patient = userRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente com ID " + patientId + " não encontrado"));
        
        User doctor = userRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Médico com ID " + doctorId + " não encontrado"));
        
        if (doctor.getRole() != UserRole.DOCTOR) {
            throw new BusinessException("Usuário com ID " + doctorId + " não é um médico");
        }
        
        // Verificar conflitos de horário
        List<Appointment> conflicts = appointmentRepository.findByDoctorAndAppointmentDate(
            doctor, appointmentDate);
        
        if (!conflicts.isEmpty()) {
            throw new BusinessException("Horário não disponível para o médico. Já existe um agendamento neste horário exato");
        }
        
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setNotes(notes);
        
        appointment = appointmentRepository.save(appointment);
        
        // Enviar notificação assíncrona
        sendNotification(appointment, "APPOINTMENT_CREATED");
        
        return appointment;
    }
    
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        User patient = userRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente com ID " + patientId + " não encontrado"));
        return appointmentRepository.findByPatient(patient);
    }
    
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        User doctor = userRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Médico com ID " + doctorId + " não encontrado"));
        
        if (doctor.getRole() != UserRole.DOCTOR) {
            throw new BusinessException("Usuário com ID " + doctorId + " não é um médico");
        }
        
        return appointmentRepository.findByDoctor(doctor);
    }
    
    public Appointment updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Agendamento com ID " + appointmentId + " não encontrado"));
        
        appointment.setStatus(status);
        appointment = appointmentRepository.save(appointment);
        
        // Enviar notificação de mudança de status
        sendNotification(appointment, "APPOINTMENT_STATUS_CHANGED");
        
        return appointment;
    }
    
    private void sendNotification(Appointment appointment, String eventType) {
        try {
            NotificationMessage message = new NotificationMessage();
            message.setAppointmentId(appointment.getId());
            message.setPatientEmail(appointment.getPatient().getEmail());
            message.setDoctorEmail(appointment.getDoctor().getEmail());
            message.setAppointmentDate(appointment.getAppointmentDate());
            message.setEventType(eventType);
            
            rabbitTemplate.convertAndSend("appointment.exchange", "appointment.notification", message);
            logger.info("Notification sent successfully for appointment: {}", appointment.getId());
        } catch (Exception e) {
            logger.error("Failed to send notification for appointment {}: {}", appointment.getId(), e.getMessage());
        }
    }
    
    @Scheduled(fixedRate = 60000) // A cada 1 minuto
    public void checkUpcomingAppointments() {
        logger.debug("Scheduler executando - {}", LocalDateTime.now());
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderStart = now.plusMinutes(5);
        LocalDateTime reminderEnd = now.plusMinutes(10);
        
        processReminders(reminderStart, reminderEnd, "APPOINTMENT_REMINDER");
    }
    
    public void sendTestReminders() {
        logger.info("Executando teste de lembretes - {}", LocalDateTime.now());
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime testEnd = now.plusHours(24);
        
        processReminders(now, testEnd, "APPOINTMENT_REMINDER");
    }
    
    private void processReminders(LocalDateTime start, LocalDateTime end, String eventType) {
        List<Appointment> appointments = appointmentRepository
            .findByAppointmentDateBetweenAndStatus(start, end, AppointmentStatus.SCHEDULED);
        
        logger.info("Encontrados {} agendamentos para lembrete entre {} e {}", 
                   appointments.size(), start, end);
        
        for (Appointment appointment : appointments) {
            logger.debug("Enviando lembrete para agendamento ID: {} - Data: {}", 
                        appointment.getId(), appointment.getAppointmentDate());
            sendNotification(appointment, eventType);
        }
    }
    
    public static class NotificationMessage {
        private Long appointmentId;
        private String patientEmail;
        private String doctorEmail;
        private LocalDateTime appointmentDate;
        private String eventType;
        
        public Long getAppointmentId() { return appointmentId; }
        public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
        public String getPatientEmail() { return patientEmail; }
        public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }
        public String getDoctorEmail() { return doctorEmail; }
        public void setDoctorEmail(String doctorEmail) { this.doctorEmail = doctorEmail; }
        public LocalDateTime getAppointmentDate() { return appointmentDate; }
        public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
    }
}
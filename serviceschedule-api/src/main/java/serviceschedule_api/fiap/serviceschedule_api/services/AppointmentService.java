package serviceschedule_api.fiap.serviceschedule_api.services;

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
        User patient = userRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente com ID " + patientId + " não encontrado"));
        
        User doctor = userRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Médico com ID " + doctorId + " não encontrado"));
        
        if (doctor.getRole() != UserRole.DOCTOR) {
            throw new BusinessException("Usuário com ID " + doctorId + " não é um médico");
        }
        
        // Verificar conflitos de horário
        List<Appointment> conflicts = appointmentRepository.findByDoctorAndDateRange(
            doctor, appointmentDate.minusMinutes(30), appointmentDate.plusMinutes(30));
        
        if (!conflicts.isEmpty()) {
            throw new BusinessException("Horário não disponível para o médico. Já existe um agendamento neste período");
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
            System.out.println("✅ Notification sent successfully for appointment: " + appointment.getId());
        } catch (Exception e) {
            System.err.println("❌ Failed to send notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Scheduled(fixedRate = 300000) // A cada 5 minutos
    public void checkUpcomingAppointments() {
        System.out.println("🔍 Scheduler executando - " + LocalDateTime.now());
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(2);
        
        List<Appointment> upcomingAppointments = appointmentRepository
            .findByAppointmentDateBetweenAndStatus(now, reminderTime, AppointmentStatus.SCHEDULED);
        
        System.out.println("📋 Encontrados " + upcomingAppointments.size() + " agendamentos para lembrete");
        
        for (Appointment appointment : upcomingAppointments) {
            System.out.println("🔔 Enviando lembrete para agendamento ID: " + appointment.getId() + 
                             " - Data: " + appointment.getAppointmentDate());
            sendNotification(appointment, "APPOINTMENT_REMINDER");
        }
    }
    
    public static class NotificationMessage {
        private Long appointmentId;
        private String patientEmail;
        private String doctorEmail;
        private LocalDateTime appointmentDate;
        private String eventType;
        
        // Getters and setters
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
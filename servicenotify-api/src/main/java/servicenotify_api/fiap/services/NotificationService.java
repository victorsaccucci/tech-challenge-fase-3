package servicenotify_api.fiap.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import servicenotify_api.fiap.entities.Notification;
import servicenotify_api.fiap.repositories.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;
    
    public NotificationService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }
    
    @RabbitListener(queues = "appointment.notification.queue")
    public void handleAppointmentNotification(AppointmentMessage message) {
        try {
            logger.info("Received notification: {} for appointment {}", 
                       sanitizeForLog(message.getEventType()), message.getAppointmentId());
            
            if (message.getAppointmentId() == null || message.getPatientEmail() == null || message.getAppointmentDate() == null) {
                logger.error("Missing required fields in message");
                return;
            }
            
            String subject = getSubjectByEventType(message.getEventType());
            String emailMessage = buildEmailMessage(message.getAppointmentDate(), message.getEventType());
            
            // Criar notificação para o paciente
            createAndSendNotification(
                message.getAppointmentId(),
                message.getPatientEmail(),
                subject,
                emailMessage,
                "EMAIL"
            );
            
            // Criar notificação para o médico se o email estiver presente
            if (message.getDoctorEmail() != null && !message.getDoctorEmail().isEmpty()) {
                createAndSendNotification(
                    message.getAppointmentId(),
                    message.getDoctorEmail(),
                    subject,
                    emailMessage,
                    "EMAIL"
                );
            }
            
            logger.info("Notification processed successfully for appointment: {}", message.getAppointmentId());
            
        } catch (Exception e) {
            logger.error("Error processing notification for appointment {}: {}", 
                        message.getAppointmentId(), e.getMessage());
        }
    }
    
    private void createAndSendNotification(Long appointmentId, String email, String subject, 
                                         String message, String type) {
        Notification notification = new Notification();
        notification.setAppointmentId(appointmentId);
        notification.setRecipientEmail(email);
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setNotificationType(type);
        notification.setStatus("PENDING");
        
        notification = notificationRepository.save(notification);
        
        sendEmailNotification(notification);
    }
    
    private void sendEmailNotification(Notification notification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getRecipientEmail());
            message.setSubject(notification.getSubject());
            message.setText(notification.getMessage());
            message.setFrom("noreply@hospital.com");
            
            mailSender.send(message);
            
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());
            
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", 
                        notification.getRecipientEmail(), e.getMessage());
            notification.setStatus("FAILED");
            notification.setErrorMessage(e.getMessage());
        }
        
        try {
            notificationRepository.save(notification);
        } catch (Exception e) {
            logger.error("Failed to save notification status: {}", e.getMessage());
        }
    }
    
    private String getSubjectByEventType(String eventType) {
        return switch (eventType) {
            case "APPOINTMENT_CREATED" -> "Consulta Agendada - Hospital";
            case "APPOINTMENT_STATUS_CHANGED" -> "Status da Consulta Alterado - Hospital";
            case "APPOINTMENT_REMINDER" -> "Lembrete de Consulta - Hospital";
            default -> "Notificação - Hospital";
        };
    }
    
    private String buildEmailMessage(LocalDateTime appointmentDate, String eventType) {
        String formattedDate = appointmentDate.format(DATE_FORMATTER);
        
        return switch (eventType) {
            case "APPOINTMENT_CREATED" -> 
                "Sua consulta foi agendada para " + formattedDate + ".\n\n" +
                "Por favor, chegue com 15 minutos de antecedência.\n\n" +
                "Em caso de dúvidas, entre em contato conosco.";
            
            case "APPOINTMENT_STATUS_CHANGED" -> 
                "O status da sua consulta de " + formattedDate + " foi alterado.\n\n" +
                "Verifique os detalhes no sistema.";
            
            case "APPOINTMENT_REMINDER" -> 
                "Lembrete: Você tem uma consulta agendada para " + formattedDate + ".\n\n" +
                "Não se esqueça de comparecer!";
            
            default -> "Você tem uma notificação sobre sua consulta de " + formattedDate + ".";
        };
    }
    
    private String sanitizeForLog(String input) {
        if (input == null) return "null";
        return input.replaceAll("[\r\n\t]", "_");
    }
    
    public static class AppointmentMessage {
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
package servicenotify_api.fiap.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicenotify_api.fiap.entities.Notification;
import servicenotify_api.fiap.repositories.NotificationRepository;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    private final NotificationRepository notificationRepository;
    
    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification service is running!");
    }
    
    @PostMapping("/test")
    public ResponseEntity<String> testNotification(@RequestBody TestNotificationRequest request) {
        try {
            // Simular uma mensagem de teste para o RabbitMQ
            String testMessage = String.format(
                "appointmentId=%d,patientEmail=%s,doctorEmail=%s,appointmentDate=%s,eventType=%s",
                request.getAppointmentId(),
                request.getPatientEmail(),
                request.getDoctorEmail() != null ? request.getDoctorEmail() : "",
                request.getAppointmentDate(),
                request.getEventType()
            );
            
            return ResponseEntity.ok("Test message format: " + testMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    public static class TestNotificationRequest {
        private Long appointmentId;
        private String patientEmail;
        private String doctorEmail;
        private String appointmentDate;
        private String eventType;
        
        // Getters and setters
        public Long getAppointmentId() { return appointmentId; }
        public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
        public String getPatientEmail() { return patientEmail; }
        public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }
        public String getDoctorEmail() { return doctorEmail; }
        public void setDoctorEmail(String doctorEmail) { this.doctorEmail = doctorEmail; }
        public String getAppointmentDate() { return appointmentDate; }
        public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
    }
}
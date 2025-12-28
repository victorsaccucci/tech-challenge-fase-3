package serviceschedule_api.fiap.serviceschedule_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.AppointmentStatus;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;
import serviceschedule_api.fiap.serviceschedule_api.repositories.UserRepository;
import serviceschedule_api.fiap.serviceschedule_api.services.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    
    public AppointmentController(AppointmentService appointmentService, UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
    }
    
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody CreateAppointmentRequest request) {
        Appointment appointment = appointmentService.createAppointment(
            request.getPatientId(), 
            request.getDoctorId(), 
            request.getAppointmentDate(), 
            request.getNotes()
        );
        return ResponseEntity.ok(appointment);
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable Long patientId, Authentication auth) {
        // Verificar se o usuário pode acessar os dados do paciente
        User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        if (currentUser == null || 
            (currentUser.getRole() == UserRole.PATIENT && !currentUser.getId().equals(patientId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable Long doctorId, Authentication auth) {
        User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        if (currentUser == null || 
            (currentUser.getRole() == UserRole.DOCTOR && !currentUser.getId().equals(doctorId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }
    
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long appointmentId, 
            @RequestBody UpdateStatusRequest request) {
        Appointment appointment = appointmentService.updateAppointmentStatus(appointmentId, request.getStatus());
        return ResponseEntity.ok(appointment);
    }
    
    public static class CreateAppointmentRequest {
        private Long patientId;
        private Long doctorId;
        private LocalDateTime appointmentDate;
        private String notes;
        
        // Getters and setters
        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }
        public Long getDoctorId() { return doctorId; }
        public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
        public LocalDateTime getAppointmentDate() { return appointmentDate; }
        public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
    
    public static class UpdateStatusRequest {
        private AppointmentStatus status;
        
        public AppointmentStatus getStatus() { return status; }
        public void setStatus(AppointmentStatus status) { this.status = status; }
    }
}
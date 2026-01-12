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
import java.util.Map;
import java.util.HashMap;

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
    public ResponseEntity<Map<String, Object>> createAppointment(@RequestBody CreateAppointmentRequest request, Authentication auth) {
        try {
            if (auth == null || auth.getName() == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Usuário não autenticado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
            if (currentUser == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Usuário não encontrado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            // Apenas médicos e enfermeiros podem criar agendamentos
            if (currentUser.getRole() == UserRole.PATIENT) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Acesso negado");
                errorResponse.put("message", "Pacientes não podem criar agendamentos. Entre em contato com a recepção.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            Appointment appointment = appointmentService.createAppointment(
                request.getPatientId(), 
                request.getDoctorId(), 
                request.getAppointmentDate(), 
                request.getNotes()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("appointment", appointment);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getPatientAppointments(@PathVariable Long patientId, Authentication auth) {
        User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        if (currentUser == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuário não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        // Regras de acesso: Médicos e Enfermeiros podem ver qualquer paciente, Pacientes só podem ver seus próprios dados
        if (currentUser.getRole() == UserRole.PATIENT && !currentUser.getId().equals(patientId)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Acesso negado");
            errorResponse.put("message", "Pacientes podem visualizar apenas suas próprias consultas");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("appointments", appointments);
        
        if (appointments.isEmpty()) {
            response.put("message", "Paciente encontrado, mas não possui agendamentos cadastrados.");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> getDoctorAppointments(@PathVariable Long doctorId, Authentication auth) {
        User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        if (currentUser == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuário não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        // Regras de acesso: Médicos só podem ver seus próprios agendamentos, Enfermeiros podem ver todos
        if (currentUser.getRole() == UserRole.DOCTOR && !currentUser.getId().equals(doctorId)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Acesso negado");
            errorResponse.put("message", "Médicos podem visualizar apenas seus próprios agendamentos");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        
        if (currentUser.getRole() == UserRole.PATIENT) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Acesso negado");
            errorResponse.put("message", "Pacientes não podem visualizar agendamentos de médicos");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("appointments", appointments);
        
        if (appointments.isEmpty()) {
            response.put("message", "Médico encontrado, mas não possui agendamentos cadastrados.");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Map<String, Object>> updateAppointmentStatus(
            @PathVariable Long appointmentId, 
            @RequestBody UpdateStatusRequest request,
            Authentication auth) {
        if (auth == null || auth.getName() == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuário não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        if (currentUser == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuário não encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        // Apenas médicos e enfermeiros podem atualizar status de agendamentos
        if (currentUser.getRole() == UserRole.PATIENT) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Acesso negado");
            errorResponse.put("message", "Pacientes não podem alterar o status de agendamentos.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        
        Appointment appointment = appointmentService.updateAppointmentStatus(appointmentId, request.getStatus());
        
        Map<String, Object> response = new HashMap<>();
        response.put("appointment", appointment);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/test-scheduler")
    public ResponseEntity<String> testScheduler() {
        System.out.println("Executando scheduler manualmente...");
        appointmentService.checkUpcomingAppointments();
        return ResponseEntity.ok("Scheduler executado - verifique os logs");
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
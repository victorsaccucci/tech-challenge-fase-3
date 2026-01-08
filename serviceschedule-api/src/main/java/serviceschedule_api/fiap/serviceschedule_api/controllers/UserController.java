package serviceschedule_api.fiap.serviceschedule_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;
import serviceschedule_api.fiap.serviceschedule_api.repositories.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserRepository userRepository;
    
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/doctors")
    public ResponseEntity<Map<String, Object>> getDoctors(Authentication auth) {
        User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        if (currentUser == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuário não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        List<User> doctors = userRepository.findByRole(UserRole.DOCTOR);
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/nurses")
    public ResponseEntity<Map<String, Object>> getNurses(Authentication auth) {
        User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        if (currentUser == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuário não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        // Apenas médicos e enfermeiros podem listar enfermeiros
        if (currentUser.getRole() == UserRole.PATIENT) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Acesso negado");
            errorResponse.put("message", "Pacientes não podem visualizar lista de enfermeiros.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        
        List<User> nurses = userRepository.findByRole(UserRole.NURSE);
        Map<String, Object> response = new HashMap<>();
        response.put("nurses", nurses);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/patients")
    public ResponseEntity<Map<String, Object>> getPatients(Authentication auth) {
        User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        if (currentUser == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuário não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        // Apenas médicos e enfermeiros podem listar pacientes
        if (currentUser.getRole() == UserRole.PATIENT) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Acesso negado");
            errorResponse.put("message", "Pacientes não podem visualizar lista de outros pacientes.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        
        List<User> patients = userRepository.findByRole(UserRole.PATIENT);
        Map<String, Object> response = new HashMap<>();
        response.put("patients", patients);
        return ResponseEntity.ok(response);
    }
}
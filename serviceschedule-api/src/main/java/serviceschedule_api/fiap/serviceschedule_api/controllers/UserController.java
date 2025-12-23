package serviceschedule_api.fiap.serviceschedule_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;
import serviceschedule_api.fiap.serviceschedule_api.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserRepository userRepository;
    
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/doctors")
    public ResponseEntity<List<User>> getDoctors() {
        List<User> doctors = userRepository.findByRole(UserRole.DOCTOR);
        return ResponseEntity.ok(doctors);
    }
    
    @GetMapping("/nurses")
    public ResponseEntity<List<User>> getNurses() {
        List<User> nurses = userRepository.findByRole(UserRole.NURSE);
        return ResponseEntity.ok(nurses);
    }
}
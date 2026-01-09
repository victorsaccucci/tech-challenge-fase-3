package serviceschedule_api.fiap.serviceschedule_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import serviceschedule_api.fiap.serviceschedule_api.common.AuthDTOs;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;
import serviceschedule_api.fiap.serviceschedule_api.repositories.UserRepository;
import serviceschedule_api.fiap.serviceschedule_api.services.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    private final UserRepository userRepository;
    
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTOs.LoginRequest request) {
        try {
            AuthDTOs.AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDTOs.RegisterRequest request, Authentication auth) {
        try {
            // Se não há autenticação, permitir apenas o primeiro usuário (admin/médico)
            if (auth == null || auth.getName() == null) {
                // Verificar se já existe algum usuário no sistema
                long userCount = userRepository.count();
                if (userCount > 0) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Acesso negado");
                    errorResponse.put("message", "Apenas usuários autenticados podem registrar novos usuários. Entre em contato com a administração.");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
                }
                // Se não há usuários, permitir criar o primeiro (deve ser médico)
                if (!UserRole.DOCTOR.equals(request.getRole())) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Primeiro usuário deve ser médico");
                    return ResponseEntity.badRequest().body(errorResponse);
                }
            } else {
                // Se há autenticação, verificar se é médico
                User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
                if (currentUser == null) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Usuário não encontrado");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
                }
                
                // Apenas médicos podem registrar qualquer tipo de usuário
                if (!UserRole.DOCTOR.equals(currentUser.getRole())) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Acesso negado");
                    errorResponse.put("message", "Apenas médicos podem registrar novos usuários no sistema.");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
                }
            }
            
            AuthDTOs.AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
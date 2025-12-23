package serviceschedule_api.fiap.serviceschedule_api.common;

import lombok.Data;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;

public class AuthDTOs {
    
    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
    
    @Data
    public static class RegisterRequest {
        private String email;
        private String password;
        private String name;
        private String cpf;
        private UserRole role;
        private String specialty;
        private String crm;
    }
    
    @Data
    public static class AuthResponse {
        private String token;
        private String email;
        private String name;
        private UserRole role;
    }
}
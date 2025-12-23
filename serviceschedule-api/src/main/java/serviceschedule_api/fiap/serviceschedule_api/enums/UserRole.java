package serviceschedule_api.fiap.serviceschedule_api.enums;

public enum UserRole {
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR"),
    NURSE("NURSE");
    
    private String role;
    
    UserRole(String role) {
        this.role = role;
    }
    
    public String getRole() {
        return role;
    }
}
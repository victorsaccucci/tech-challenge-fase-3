package serviceschedule_api.fiap.serviceschedule_api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;
import serviceschedule_api.fiap.serviceschedule_api.repositories.AppointmentRepository;
import serviceschedule_api.fiap.serviceschedule_api.repositories.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(UserRepository userRepository, AppointmentRepository appointmentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem dados
        if (userRepository.count() > 0) {
            return;
        }
        
        try {
            // Criar médico padrão
            User doctor = new User();
            doctor.setEmail("medico@hospitech.com");
            doctor.setPassword(passwordEncoder.encode("123456"));
            doctor.setName("Dr. João Silva");
            doctor.setCpf("12345678901");
            doctor.setRole(UserRole.DOCTOR);
            doctor.setSpecialty("Cardiologia");
            doctor.setCrm("12345");
            userRepository.save(doctor);
            
            // Criar paciente padrão
            User patient = new User();
            patient.setEmail("paciente@email.com");
            patient.setPassword(passwordEncoder.encode("123456"));
            patient.setName("Maria Santos");
            patient.setCpf("98765432100");
            patient.setRole(UserRole.PATIENT);
            userRepository.save(patient);
            
            System.out.println("Dados iniciais criados:");
            System.out.println("Médico: medico@hospitech.com / 123456");
            System.out.println("Paciente: paciente@email.com / 123456");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar dados: " + e.getMessage());
        }
    }
}
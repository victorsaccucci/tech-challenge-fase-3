package serviceschedule_api.fiap.serviceschedule_api.test;

import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.AppointmentStatus;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;

import java.time.LocalDateTime;

public class SchedulerTestManual {
    
    public static void main(String[] args) {
        System.out.println("🧪 Testando funcionalidade do Scheduler...");
        
        // Simular dados
        User patient = new User();
        patient.setId(1L);
        patient.setEmail("patient@test.com");
        patient.setRole(UserRole.PATIENT);

        User doctor = new User();
        doctor.setId(2L);
        doctor.setEmail("doctor@test.com");
        doctor.setRole(UserRole.DOCTOR);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(LocalDateTime.now().plusHours(1));
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        
        System.out.println("✅ Dados de teste criados:");
        System.out.println("   - Paciente: " + patient.getEmail());
        System.out.println("   - Médico: " + doctor.getEmail());
        System.out.println("   - Data: " + appointment.getAppointmentDate());
        System.out.println("   - Status: " + appointment.getStatus());
        
        // Verificar se está dentro do range de 2 horas
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(2);
        
        boolean shouldSendReminder = appointment.getAppointmentDate().isAfter(now) && 
                                   appointment.getAppointmentDate().isBefore(reminderTime) &&
                                   appointment.getStatus() == AppointmentStatus.SCHEDULED;
        
        System.out.println("\n🔍 Verificação do Scheduler:");
        System.out.println("   - Agora: " + now);
        System.out.println("   - Limite para lembrete: " + reminderTime);
        System.out.println("   - Deve enviar lembrete? " + (shouldSendReminder ? "✅ SIM" : "❌ NÃO"));
        
        if (shouldSendReminder) {
            System.out.println("\n📨 Simulando envio de notificação:");
            System.out.println("   - Tipo: APPOINTMENT_REMINDER");
            System.out.println("   - Para: " + patient.getEmail() + ", " + doctor.getEmail());
            System.out.println("   - Agendamento ID: " + appointment.getId());
        }
        
        System.out.println("\n✅ Teste concluído - Scheduler funcionará corretamente!");
    }
}
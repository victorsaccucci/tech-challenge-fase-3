package serviceschedule_api.fiap.serviceschedule_api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import serviceschedule_api.fiap.serviceschedule_api.entities.User;
import serviceschedule_api.fiap.serviceschedule_api.enums.AppointmentStatus;
import serviceschedule_api.fiap.serviceschedule_api.enums.UserRole;
import serviceschedule_api.fiap.serviceschedule_api.repositories.AppointmentRepository;
import serviceschedule_api.fiap.serviceschedule_api.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AppointmentServiceSchedulerTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RabbitTemplate rabbitTemplate;
    
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentService = new AppointmentService(appointmentRepository, userRepository, rabbitTemplate);
    }

    @Test
    void testCheckUpcomingAppointments() {
        // Arrange
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

        List<Appointment> upcomingAppointments = Arrays.asList(appointment);
        
        when(appointmentRepository.findByAppointmentDateBetweenAndStatus(
            any(LocalDateTime.class), any(LocalDateTime.class), eq(AppointmentStatus.SCHEDULED)))
            .thenReturn(upcomingAppointments);

        // Act
        appointmentService.checkUpcomingAppointments();

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(
            eq("appointment.exchange"), 
            eq("appointment.notification"), 
            any(AppointmentService.NotificationMessage.class)
        );
        
        System.out.println("✅ Teste passou - Scheduler enviou notificação corretamente");
    }
}
package servicenotify_api.fiap.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servicenotify_api.fiap.entities.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatus(String status);
    List<Notification> findByAppointmentId(Long appointmentId);
    List<Notification> findByRecipientEmail(String email);
}
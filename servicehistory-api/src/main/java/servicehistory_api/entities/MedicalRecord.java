package servicehistory_api.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long patientId;
    
    @Column(nullable = false)
    private Long doctorId;
    
    @Column(nullable = false)
    private Long appointmentId;
    
    @Column(nullable = false, length = 2000)
    private String diagnosis;
    
    @Column(length = 2000)
    private String treatment;
    
    @Column(length = 1000)
    private String medications;
    
    @Column(length = 1000)
    private String observations;
    
    @Column(nullable = false)
    private LocalDateTime recordDate = LocalDateTime.now();
    
    @Column
    private LocalDateTime followUpDate;
}
package serviceschedule_api.fiap.serviceschedule_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    // Cada paciente pode ter várias consultas
    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private List<Appointment> appointments;
}

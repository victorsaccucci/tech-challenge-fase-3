package cuidei_api.fiap.cuidei_api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "doctor")
@Data
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialty;
}

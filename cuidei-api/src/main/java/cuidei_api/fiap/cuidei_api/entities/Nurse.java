package cuidei_api.fiap.cuidei_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "nurses")
public class Nurse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String registrationNumber;

    @OneToMany(mappedBy = "nurse")
    @JsonIgnore
    private List<Appointment> appointments; // Consultas que este enfermeiro registrou
}

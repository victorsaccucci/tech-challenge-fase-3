package cuidei_api.fiap.cuidei_api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "patient")
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cpf;
    private String email;
}

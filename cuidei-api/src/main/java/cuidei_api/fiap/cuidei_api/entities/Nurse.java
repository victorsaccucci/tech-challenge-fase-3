package cuidei_api.fiap.cuidei_api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "nurse")
@Data
public class Nurse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}

package cuidei_api.fiap.cuidei_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "doctors")
@Data
public class Doctor extends User {

    private String specialty;
}

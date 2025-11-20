package cuidei_api.fiap.cuidei_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "patients")
@Data
public class Patient extends User {

    private String cpf;
}

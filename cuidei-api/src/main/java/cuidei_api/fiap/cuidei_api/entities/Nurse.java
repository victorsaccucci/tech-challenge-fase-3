package cuidei_api.fiap.cuidei_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "nurses")
@Data
public class Nurse extends User {

    private String registrationNumber;
}

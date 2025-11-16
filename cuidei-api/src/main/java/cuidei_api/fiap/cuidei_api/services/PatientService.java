package cuidei_api.fiap.cuidei_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cuidei_api.fiap.cuidei_api.entities.Patient;
import cuidei_api.fiap.cuidei_api.repositories.PatientRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public void delete(Long id) {
        patientRepository.deleteById(id);
    }

    public Patient update(Long id, Patient patient) {
        Patient existing = findById(id);
        existing.setName(patient.getName());
        existing.setCpf(patient.getCpf());
        existing.setEmail(patient.getEmail());
        return patientRepository.save(existing);
    }
}

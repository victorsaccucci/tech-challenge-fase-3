package cuidei_api.fiap.cuidei_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cuidei_api.fiap.cuidei_api.entities.Doctor;
import cuidei_api.fiap.cuidei_api.repositories.DoctorRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }

    public Doctor update(Long id, Doctor doctor) {
        Doctor existing = findById(id);
        existing.setName(doctor.getName());
        existing.setSpecialty(doctor.getSpecialty());
        return doctorRepository.save(existing);
    }
}

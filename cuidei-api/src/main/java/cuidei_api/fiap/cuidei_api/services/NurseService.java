package cuidei_api.fiap.cuidei_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cuidei_api.fiap.cuidei_api.entities.Nurse;
import cuidei_api.fiap.cuidei_api.repositories.NurseRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NurseService {

    private final NurseRepository nurseRepository;

    public List<Nurse> findAll() {
        return nurseRepository.findAll();
    }

    public Nurse findById(Long id) {
        return nurseRepository.findById(id).orElseThrow(() -> new RuntimeException("Nurse not found"));
    }

    public Nurse save(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    public void delete(Long id) {
        nurseRepository.deleteById(id);
    }

    public Nurse update(Long id, Nurse nurse) {
        Nurse existing = findById(id);
        existing.setName(nurse.getName());
        return nurseRepository.save(existing);
    }

}

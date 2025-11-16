package cuidei_api.fiap.cuidei_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cuidei_api.fiap.cuidei_api.entities.Patient;
import cuidei_api.fiap.cuidei_api.services.PatientService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/patients")
@AllArgsConstructor
public class PatientController {

    private final PatientService service;

    @GetMapping
    public ResponseEntity<?> listPatients() {
        List<Patient> patients = service.findAll();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public Patient get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping 
    public Patient create(@RequestBody Patient patient) {
        return service.save(patient);
    }

    @PutMapping("/{id}")
    public Patient update(@PathVariable Long id, @RequestBody Patient patient) {
        return service.update(id, patient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}

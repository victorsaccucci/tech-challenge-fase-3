package cuidei_api.fiap.cuidei_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cuidei_api.fiap.cuidei_api.entities.Doctor;
import cuidei_api.fiap.cuidei_api.services.DoctorService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/doctors")
@AllArgsConstructor
public class DoctorController {

    private final DoctorService service;

    @GetMapping
    public ResponseEntity<?> listDoctors() {
        List<Doctor> doctors = service.findAll();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public Doctor get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping 
    public Doctor create(@RequestBody Doctor doctor) {
        return service.save(doctor);
    }

    @PutMapping("/{id}")
    public Doctor update(@PathVariable Long id, @RequestBody Doctor doctor) {
        return service.update(id, doctor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

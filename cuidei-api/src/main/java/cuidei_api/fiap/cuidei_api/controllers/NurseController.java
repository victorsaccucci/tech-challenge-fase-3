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

import cuidei_api.fiap.cuidei_api.entities.Nurse;
import cuidei_api.fiap.cuidei_api.services.NurseService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/nurses")
@AllArgsConstructor
public class NurseController {

    private final NurseService service;

    @GetMapping
    public ResponseEntity<?> listNurses() {
        List<Nurse> nurses = service.findAll();
        return ResponseEntity.ok(nurses);
    }

    @GetMapping("/{id}")
    public Nurse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping 
    public Nurse create(@RequestBody Nurse nurse) {
        return service.save(nurse);
    }

    @PutMapping("/{id}")
    public Nurse update(@PathVariable Long id, @RequestBody Nurse nurse) {
        return service.update(id, nurse);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

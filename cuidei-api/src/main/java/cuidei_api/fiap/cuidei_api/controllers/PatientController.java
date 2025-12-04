package cuidei_api.fiap.cuidei_api.controllers;

import cuidei_api.fiap.cuidei_api.services.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> getMyAppointments(Authentication auth) {

        String loggedCpf = auth.getName();

        return ResponseEntity.ok(patientService.getLoggedPatientAppointments(loggedCpf));
    }
}

package cuidei_api.fiap.cuidei_api.controllers;

import cuidei_api.fiap.cuidei_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/patient")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT')")
    public ResponseEntity<?> profile(Authentication auth) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "You accessed the doctor route",
                        "user", auth.getName()
                )
        );
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<?> doctorArea(Authentication auth) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "You accessed the doctor route",
                        "user", auth.getName()
                )
        );
    }

    @GetMapping("/nurse")
    @PreAuthorize("hasRole('ROLE_NURSE')")
    public ResponseEntity<?> nurseArea(Authentication auth) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "You accessed the nurse route",
                        "user", auth.getName()
                )
        );
    }
}


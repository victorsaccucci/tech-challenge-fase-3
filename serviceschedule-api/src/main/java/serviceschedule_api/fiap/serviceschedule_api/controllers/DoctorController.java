package serviceschedule_api.fiap.serviceschedule_api.controllers;

import serviceschedule_api.fiap.serviceschedule_api.common.swagger.openapi.controller.DoctorApi;
import serviceschedule_api.fiap.serviceschedule_api.common.swagger.openapi.dto.AppointmentDtoApi;
import serviceschedule_api.fiap.serviceschedule_api.common.swagger.openapi.dto.DoctorUpdateAppointmentDtoApi;
import serviceschedule_api.fiap.serviceschedule_api.entities.Appointment;
import serviceschedule_api.fiap.serviceschedule_api.mapper.AppointmentMapper;
import serviceschedule_api.fiap.serviceschedule_api.services.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController implements DoctorApi {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentDtoApi>> getAppointments(Long id) {
        List<Appointment> appointments = doctorService.findAppointments(id);
        return ResponseEntity.ok(AppointmentMapper.toDtoList(appointments));
    }

    @Override
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentDtoApi> updateAppointment(Long id, DoctorUpdateAppointmentDtoApi dto) {
        Appointment incoming = AppointmentMapper.toEntityFromDoctorUpdate(dto);
        Appointment updated = doctorService.updateAppointment(id, incoming);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(AppointmentMapper.toDto(updated));
    }

}

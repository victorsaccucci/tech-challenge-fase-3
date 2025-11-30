package cuidei_api.fiap.cuidei_api.controllers;

import cuidei_api.fiap.cuidei_api.common.config.swagger.openapi.controller.NurseApi;
import cuidei_api.fiap.cuidei_api.common.config.swagger.openapi.dto.AppointmentDtoApi;
import cuidei_api.fiap.cuidei_api.entities.Appointment;
import cuidei_api.fiap.cuidei_api.mapper.AppointmentMapper;
import cuidei_api.fiap.cuidei_api.services.NurseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nurse")
public class NurseController implements NurseApi {

    private final NurseService nurseService;
    private AppointmentMapper appointmentMapper;

    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;

    }

    /**
     * Retorna todas as consultas associadas a uma enfermeira específica.
     * @param id o identificador único da enfermeira.
     * @return uma ResponseEntity contendo a lista de AppointmentDtoApi convertidos.
     */
    @Override
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<List<AppointmentDtoApi>> getAppointments(@PathVariable Long id) {
        List<Appointment> appointments = nurseService.findAppointments(id);
        return ResponseEntity.ok(AppointmentMapper.toDtoList(appointments));
    }

    /**
     * Registra um novo agendamento realizado por uma enfermeira.
     * @param appointment DTO contendo os dados da consulta a ser registrada.
     * @return uma ResponseEntity contendo o AppointmentDtoApi salvo.
     */
    @Override
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<AppointmentDtoApi> registerAppointment(@RequestBody AppointmentDtoApi appointment) {
        Appointment entity = AppointmentMapper.toEntity(appointment);
        Appointment saved = nurseService.registerAppointment(entity);

        return ResponseEntity.ok(AppointmentMapper.toDto(saved));
    }
}

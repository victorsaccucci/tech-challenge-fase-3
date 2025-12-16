package serviceschedule_api.fiap.serviceschedule_api.common.swagger.openapi.controller;

import serviceschedule_api.fiap.serviceschedule_api.common.swagger.openapi.dto.AppointmentDtoApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface NurseApi {

    @Operation(summary = "Lista consultas registradas por uma enfermeira", description = "Retorna todas as consultas associadas à enfermeira informada pelo ID.")
    @ApiResponse(responseCode = "200", description = "Consultas encontradas")
    @ApiResponse(responseCode = "404", description = "Enfermeira não encontrada")

    @GetMapping("/{id}/appointments")
    ResponseEntity<List<AppointmentDtoApi>> getAppointments(
            @Parameter(description = "ID da enfermeira")
            @PathVariable Long id
    );

    @Operation(summary = "Registra uma nova consulta", description = "Cria um novo agendamento. Se o paciente não existir, ele será criado automaticamente.")
    @ApiResponse(responseCode = "200", description = "Consulta registrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")

    @PostMapping("/appointment")
    ResponseEntity<AppointmentDtoApi> registerAppointment(
            @Parameter(description = "Dados da consulta", required = true)
            @RequestBody AppointmentDtoApi appointment
    );
}

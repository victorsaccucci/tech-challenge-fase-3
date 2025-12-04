package cuidei_api.fiap.cuidei_api.common.swagger.openapi.controller;

import cuidei_api.fiap.cuidei_api.common.swagger.openapi.dto.AppointmentDtoApi;
import cuidei_api.fiap.cuidei_api.common.swagger.openapi.dto.DoctorUpdateAppointmentDtoApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

public interface DoctorApi {

        @Operation(
                summary = "Lista consultas atendidas pelo médico",
                description = "Retorna o histórico completo de consultas associadas ao médico informado."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200",
                        description = "Consultas encontradas",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDtoApi.class))
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Médico não encontrado"
                )
        })
        @GetMapping("/{id}/appointments")
        ResponseEntity<List<AppointmentDtoApi>> getAppointments(
                @Parameter(description = "ID do médico", example = "1") @PathVariable Long id
        );


        @Operation(
                summary = "Atualiza uma consulta",
                description = "Permite que o médico altere os dados de um agendamento existente."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Consulta atualizada", content = @Content(schema = @Schema(implementation = AppointmentDtoApi.class))),
                @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos enviados")
        })
        @PutMapping("/appointment/{id}")
        ResponseEntity<AppointmentDtoApi> updateAppointment(
                @Parameter(description = "ID da consulta", example = "8") @PathVariable Long id,
                @RequestBody DoctorUpdateAppointmentDtoApi appointment
        );
}

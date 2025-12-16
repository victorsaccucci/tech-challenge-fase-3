package serviceschedule_api.fiap.serviceschedule_api.common.swagger.openapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Dados de um agendamento")
public class AppointmentDtoApi {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Dados do médico responsável")
    private DoctorDtoApi doctor;

    @Schema(description = "Dados do paciente")
    private PatientDtoApi patient;

    @Schema(description = "Enfermeira que registrou o agendamento")
    private NurseDtoApi nurse;

    @Schema(description = "Data e hora do agendamento", example = "2025-11-30T14:30:00")
    private LocalDateTime dateTime;

    @Schema(description = "Motivo da consulta", example = "Retorno pós-operatório")
    private String reason;

}

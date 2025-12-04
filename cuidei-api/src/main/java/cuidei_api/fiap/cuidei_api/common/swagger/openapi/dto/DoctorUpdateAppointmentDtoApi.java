package cuidei_api.fiap.cuidei_api.common.swagger.openapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados que o médico pode atualizar em uma consulta existente")
public class DoctorUpdateAppointmentDtoApi {

    @Schema(
            description = "Dados atualizáveis do paciente. "
                    + "Se informado sem ID, atualizará os dados do paciente já vinculado à consulta.",
            implementation = PatientDtoApi.class
    )
    private PatientDtoApi patient;

    @Schema(
            description = "Nova data e hora da consulta",
            example = "2025-11-30T14:30:00"
    )
    private String dateTime;

    @Schema(
            description = "Novo motivo da consulta",
            example = "Retorno pós-operatório"
    )
    private String reason;
}

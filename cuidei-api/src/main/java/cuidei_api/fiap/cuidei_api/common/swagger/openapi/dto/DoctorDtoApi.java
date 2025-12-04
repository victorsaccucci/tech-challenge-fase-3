package cuidei_api.fiap.cuidei_api.common.swagger.openapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "Informações do médico")
public class DoctorDtoApi {

    @Schema(description = "ID do médico", example = "1")
    private Long id;

    @Schema(description = "Nome do médico", example = "Dr. João da Silva")
    private String name;

    @Schema(description = "Número do CRM", example = "CRM123456")
    private String crm;

    @Schema(description = "Especialidade médica", example = "Cardiologia")
    private String specialty;
}

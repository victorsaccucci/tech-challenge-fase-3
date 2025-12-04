package cuidei_api.fiap.cuidei_api.common.swagger.openapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Informações do paciente")
public class PatientDtoApi {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nome do paciente", example = "Lucas Ferreira")
    private String name;

    @Schema(description = "CPF do paciente", example = "12345678901")
    private String cpf;

    @Schema(description = "Email do paciente", example = "lucas@gmail.com")
    private String email;


}

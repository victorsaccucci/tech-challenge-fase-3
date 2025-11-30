package cuidei_api.fiap.cuidei_api.common.config.swagger.openapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Informações da enfermeira")
public class NurseDtoApi {

    @Schema(description = "ID da enfermeira", example = "3")
    private Long id;

    @Schema(description = "Nome da enfermeira", example = "Enfermeira Ana Costa")
    private String name;

    @Schema(description = "Número de registro profissional", example = "REG98765")
    private String registrationNumber;
}

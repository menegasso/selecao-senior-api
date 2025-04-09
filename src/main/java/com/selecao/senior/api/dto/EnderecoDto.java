package com.selecao.senior.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDto {

    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "O tipo de logradouro é obrigatório.")
    @Size(max = 50, message = "O tipo de logradouro deve ter no máximo 50 caracteres.")
    private String tipoLogradouro;

    @NotBlank(message = "O logradouro é obrigatório.")
    @Size(max = 200, message = "O logradouro deve ter no máximo 200 caracteres.")
    private String logradouro;

    @NotNull(message = "O número é obrigatório.")
    private Long numero;

    @NotBlank(message = "O bairro é obrigatório.")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres.")
    private String bairro;

    @NotNull(message = "O id da cidade é obrigatório.")
    private Long cidadeId;
}

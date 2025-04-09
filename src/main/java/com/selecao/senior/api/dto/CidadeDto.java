package com.selecao.senior.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CidadeDto {

    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres.")
    private String nome;

    @NotBlank(message = "O campo UF é obrigatório.")
    @Size(min = 2, max = 2, message = "UF deve ter exatamente 2 caracteres.")
    @Pattern(regexp = "^[A-Z]{2}$", message = "UF deve conter duas letras maiúsculas.")
    private String uf;
}

package com.selecao.senior.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeDto {

    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "O nome da unidade é obrigatório.")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres.")
    private String nome;

    @NotBlank(message = "A sigla da unidade é obrigatória.")
    @Size(max = 20, message = "A sigla deve ter no máximo 20 caracteres.")
    private String sigla;

    private List<EnderecoDto> enderecos;
}

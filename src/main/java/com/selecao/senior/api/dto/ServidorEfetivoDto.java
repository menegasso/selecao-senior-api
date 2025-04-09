package com.selecao.senior.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServidorEfetivoDto {
    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres.")
    private String nome;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser uma data passada.")
    private LocalDate dataNascimento;

    @NotBlank(message = "O sexo é obrigatório.")
    @Size(max = 9, message = "O sexo deve ter no máximo 9 caracteres.")
    private String sexo;

    @NotBlank(message = "O nome da mãe é obrigatório.")
    @Size(max = 200, message = "O nome da mãe deve ter no máximo 200 caracteres.")
    private String mae;

    @NotBlank(message = "O nome do pai é obrigatório.")
    @Size(max = 200, message = "O nome do pai deve ter no máximo 200 caracteres.")
    private String pai;

    @NotBlank(message = "A matrícula é obrigatória.")
    @Size(max = 20, message = "A matrícula deve ter no máximo 20 caracteres.")
    private String matricula;
}

package com.selecao.senior.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FotoPessoaDto {

    @Schema(hidden = true)
    private Integer id;

    @NotNull(message = "O id da pessoa é obrigatório.")
    private Long pessoaId;

    @NotNull(message = "A data é obrigatória.")
    private LocalDate data;

    @NotBlank(message = "O bucket é obrigatório.")
    @Size(max = 50, message = "O bucket deve ter no máximo 50 caracteres.")
    private String bucket;

    @NotBlank(message = "O hash é obrigatório.")
    @Size(max = 50, message = "O hash deve ter no máximo 50 caracteres.")
    private String hash;
}

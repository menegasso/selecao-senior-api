package com.selecao.senior.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotacaoDto {

    @Schema(hidden = true)
    private Long id;

    @NotNull(message = "O id da pessoa é obrigatório.")
    private Long pessoaId;

    @NotNull(message = "O id da unidade é obrigatório.")
    private Long unidId;

    @NotNull(message = "A data de lotação é obrigatória.")
    @PastOrPresent(message = "A data de lotação deve ser uma data passada ou presente.")
    private LocalDate dataLotacao;

    // A data de remoção é opcional
    private LocalDate dataRemocao;

    @NotBlank(message = "A portaria é obrigatória.")
    @Size(max = 100, message = "A portaria deve ter no máximo 100 caracteres.")
    private String portaria;
}

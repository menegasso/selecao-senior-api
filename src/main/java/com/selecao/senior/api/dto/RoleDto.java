package com.selecao.senior.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private Long id;

    @NotBlank(message = "O nome da role é obrigatório.")
    private String nome;
}

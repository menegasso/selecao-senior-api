package com.selecao.senior.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {

    private Long id;

    @NotBlank(message = "O username é obrigatório.")
    private String username;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String senha;

    private List<RoleDto> roles;
}

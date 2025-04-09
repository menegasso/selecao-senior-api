package com.selecao.senior.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "O username é obrigatório.")
    private String username;

    @NotBlank(message = "A senha é obrigatória.")
    private String password;
}

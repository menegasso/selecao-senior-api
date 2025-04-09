package com.selecao.senior.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados do Servidor Efetivo Lotado")
public class ServidorEfetivoLotacaoDto {
    private String nome;
    private int idade;
    private String unidade;
    private String fotoUrl;
}

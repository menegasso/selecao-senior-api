package com.selecao.senior.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "servidor_efetivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "pes_id")
public class ServidorEfetivo extends Pessoa {
    @Column(name = "se_matricula", nullable = false, unique = true, length = 20)
    private String matricula;
}

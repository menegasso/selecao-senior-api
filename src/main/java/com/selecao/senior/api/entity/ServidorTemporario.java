package com.selecao.senior.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "servidor_temporario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "pes_id")
public class ServidorTemporario extends Pessoa {

    @Column(name = "st_data_admissao", nullable = false)
    private java.time.LocalDate dataAdmissao;

    @Column(name = "st_data_demissao")
    private java.time.LocalDate dataDemissao;
}

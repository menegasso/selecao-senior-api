package com.selecao.senior.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "lotacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lot_id")
    private Long id;

    // Relacionamento com Pessoa: cada lotação está associada a uma pessoa
    @ManyToOne(optional = false)
    @JoinColumn(name = "pes_id", nullable = false)
    private Pessoa pessoa;

    // Relacionamento com Unidade: cada lotação está associada a uma unidade
    @ManyToOne(optional = false)
    @JoinColumn(name = "unid_id", nullable = false)
    private Unidade unidade;

    @Column(name = "lot_data_lotacao", nullable = false)
    private LocalDate dataLotacao;

    @Column(name = "lot_data_remocao")
    private LocalDate dataRemocao;

    @Column(name = "lot_portaria", length = 100)
    private String portaria;
}

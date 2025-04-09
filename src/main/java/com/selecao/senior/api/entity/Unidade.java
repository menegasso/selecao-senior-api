package com.selecao.senior.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unid_id")
    private Long id;

    @Column(name = "unid_nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "unid_sigla", nullable = false, length = 20)
    private String sigla;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "unidade_endereco",
            joinColumns = @JoinColumn(name = "uni_id"),
            inverseJoinColumns = @JoinColumn(name = "end_id")
    )
    private List<Endereco> enderecos = new ArrayList<>();
}

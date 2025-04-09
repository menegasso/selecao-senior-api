package com.selecao.senior.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid_id")
    private Long id;

    @Column(name = "cid_nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "cid_uf", nullable = false, columnDefinition = "char(2)")
    private String uf;
}

package com.selecao.senior.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pes_id")
    private Long id;

    @Column(name = "pes_nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "pes_data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "pes_sexo", nullable = false, length = 9)
    private String sexo;

    @Column(name = "pes_mae", nullable = false, length = 200)
    private String mae;

    @Column(name = "pes_pai", nullable = false, length = 200)
    private String pai;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "pessoa_endereco",
            joinColumns = @JoinColumn(name = "pes_id"),
            inverseJoinColumns = @JoinColumn(name = "end_id")
    )
    private List<Endereco> enderecos = new ArrayList<>();
}

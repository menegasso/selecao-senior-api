package com.selecao.senior.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "foto_pessoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FotoPessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fp_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pes_id", nullable = false)
    private Pessoa pessoa;

    @Column(name = "fp_data", nullable = false)
    private LocalDate data;

    @Column(name = "fp_bucket", nullable = false, length = 50)
    private String bucket;

    @Column(name = "fp_hash", nullable = false, length = 50)
    private String hash;
}

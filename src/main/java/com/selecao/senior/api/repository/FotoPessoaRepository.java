package com.selecao.senior.api.repository;

import com.selecao.senior.api.entity.FotoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoPessoaRepository extends JpaRepository<FotoPessoa, Integer> {
    FotoPessoa findTopByPessoaIdOrderByDataDesc(Long pessoaId);
}

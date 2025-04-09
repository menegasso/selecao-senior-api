package com.selecao.senior.api.repository;

import com.selecao.senior.api.entity.Lotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotacaoRepository extends JpaRepository<Lotacao, Long> {

    @Query("SELECT l FROM Lotacao l " +
            "WHERE l.unidade.id = :unidId " +
            "  AND l.dataRemocao IS NULL " +
            "  AND TYPE(l.pessoa) = com.selecao.senior.api.entity.ServidorEfetivo")
    List<Lotacao> findServidoresEfetivosByUnidade(@Param("unidId") Long unidId);

    @Query("SELECT l FROM Lotacao l WHERE l.pessoa.id = :pessoaId AND l.dataRemocao IS NULL")
    Lotacao findByPessoaIdAndDataRemocaoIsNull(@Param("pessoaId") Long pessoaId);

}

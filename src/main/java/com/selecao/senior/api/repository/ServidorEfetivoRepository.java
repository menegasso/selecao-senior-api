package com.selecao.senior.api.repository;

import com.selecao.senior.api.entity.ServidorEfetivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServidorEfetivoRepository extends JpaRepository<ServidorEfetivo, Long> {
    List<ServidorEfetivo> findByNomeContainingIgnoreCase(String nome);
}

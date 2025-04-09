package com.selecao.senior.api.repository;

import com.selecao.senior.api.entity.ServidorTemporario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServidorTemporarioRepository extends JpaRepository<ServidorTemporario, Long> {}

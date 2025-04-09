package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.ServidorEfetivoDto;
import com.selecao.senior.api.entity.ServidorEfetivo;
import com.selecao.senior.api.exception.ResourceNotFoundException;
import com.selecao.senior.api.repository.ServidorEfetivoRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class ServidorEfetivoService {

    @Autowired
    private ServidorEfetivoRepository repository;

    public Page<ServidorEfetivoDto> findAll(Pageable pageable) {
        Page<ServidorEfetivo> page = repository.findAll(pageable);
        return page.map(this::convertToDto);
    }

    public ServidorEfetivoDto findById(Long id) {
        ServidorEfetivo se = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor efetivo não encontrado com id: " + id));
        return convertToDto(se);
    }

    public ServidorEfetivoDto create(@Valid ServidorEfetivoDto dto) {
        try {
            ServidorEfetivo se = convertToEntity(dto);
            ServidorEfetivo saved = repository.save(se);
            return convertToDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para criar Servidor efetivo.", ex);
        }
    }

    @Transactional
    public ServidorEfetivoDto update(Long id, @Valid ServidorEfetivoDto dto) {
        ServidorEfetivo se = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor efetivo não encontrado com id: " + id));
        se.setNome(dto.getNome());
        se.setDataNascimento(dto.getDataNascimento());
        se.setSexo(dto.getSexo());
        se.setMae(dto.getMae());
        se.setPai(dto.getPai());
        se.setMatricula(dto.getMatricula());
        try {
            ServidorEfetivo updated = repository.save(se);
            return convertToDto(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para atualizar Servidor efetivo.", ex);
        }
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Servidor efetivo não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    private ServidorEfetivoDto convertToDto(ServidorEfetivo se) {
        ServidorEfetivoDto dto = new ServidorEfetivoDto();
        dto.setId(Long.valueOf(se.getId()));
        dto.setNome(se.getNome());
        dto.setDataNascimento(se.getDataNascimento());
        dto.setSexo(se.getSexo());
        dto.setMae(se.getMae());
        dto.setPai(se.getPai());
        dto.setMatricula(se.getMatricula());
        return dto;
    }

    private ServidorEfetivo convertToEntity(ServidorEfetivoDto dto) {
        ServidorEfetivo se = new ServidorEfetivo();
        se.setNome(dto.getNome());
        se.setDataNascimento(dto.getDataNascimento());
        se.setSexo(dto.getSexo());
        se.setMae(dto.getMae());
        se.setPai(dto.getPai());
        se.setMatricula(dto.getMatricula());
        return se;
    }
}

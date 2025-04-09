package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.ServidorTemporarioDto;
import com.selecao.senior.api.entity.ServidorTemporario;
import com.selecao.senior.api.exception.ResourceNotFoundException;
import com.selecao.senior.api.repository.ServidorTemporarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class ServidorTemporarioService {

    @Autowired
    private ServidorTemporarioRepository repository;

    public List<ServidorTemporarioDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ServidorTemporarioDto findById(Long id) {
        ServidorTemporario st = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor temporário não encontrado com id: " + id));
        return convertToDto(st);
    }

    public ServidorTemporarioDto create(@Valid ServidorTemporarioDto dto) {
        try {
            ServidorTemporario st = convertToEntity(dto);
            ServidorTemporario saved = repository.save(st);
            return convertToDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para criar Servidor temporário.", ex);
        }
    }

    @Transactional
    public ServidorTemporarioDto update(Long id, @Valid ServidorTemporarioDto dto) {
        ServidorTemporario st = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor temporário não encontrado com id: " + id));
        st.setNome(dto.getNome());
        st.setDataNascimento(dto.getDataNascimento());
        st.setSexo(dto.getSexo());
        st.setMae(dto.getMae());
        st.setPai(dto.getPai());
        st.setDataAdmissao(dto.getDataAdmissao());
        st.setDataDemissao(dto.getDataDemissao());
        try {
            ServidorTemporario updated = repository.save(st);
            return convertToDto(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para atualizar Servidor temporário.", ex);
        }
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Servidor temporário não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    private ServidorTemporarioDto convertToDto(ServidorTemporario st) {
        ServidorTemporarioDto dto = new ServidorTemporarioDto();
        dto.setId(st.getId());
        dto.setNome(st.getNome());
        dto.setDataNascimento(st.getDataNascimento());
        dto.setSexo(st.getSexo());
        dto.setMae(st.getMae());
        dto.setPai(st.getPai());
        dto.setDataAdmissao(st.getDataAdmissao());
        dto.setDataDemissao(st.getDataDemissao());
        return dto;
    }

    private ServidorTemporario convertToEntity(ServidorTemporarioDto dto) {
        ServidorTemporario st = new ServidorTemporario();
        st.setNome(dto.getNome());
        st.setDataNascimento(dto.getDataNascimento());
        st.setSexo(dto.getSexo());
        st.setMae(dto.getMae());
        st.setPai(dto.getPai());
        st.setDataAdmissao(dto.getDataAdmissao());
        st.setDataDemissao(dto.getDataDemissao());
        return st;
    }
}

package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.CidadeDto;
import com.selecao.senior.api.entity.Cidade;
import com.selecao.senior.api.exception.ResourceNotFoundException;
import com.selecao.senior.api.repository.CidadeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    public Page<CidadeDto> findAll(Pageable pageable) {
        Page<Cidade> cidades = cidadeRepository.findAll(pageable);
        return cidades.map(this::convertToDto);
    }

    public CidadeDto findById(Long id) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada com id: " + id));
        return convertToDto(cidade);
    }

    public CidadeDto create(@Valid CidadeDto cidadeDto) {
        try {
            Cidade cidade = convertToEntity(cidadeDto);
            Cidade saved = cidadeRepository.save(cidade);
            return convertToDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para criar Cidade.", ex);
        }
    }

    @Transactional
    public CidadeDto update(Long id, @Valid CidadeDto cidadeDto) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada com id: " + id));

        cidade.setNome(cidadeDto.getNome());
        cidade.setUf(cidadeDto.getUf());

        try {
            Cidade updated = cidadeRepository.save(cidade);
            return convertToDto(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para atualizar Cidade.", ex);
        }
    }

    public void delete(Long id) {
        if (!cidadeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cidade não encontrada com id: " + id);
        }
        cidadeRepository.deleteById(id);
    }

    private CidadeDto convertToDto(Cidade cidade) {
        CidadeDto dto = new CidadeDto();
        dto.setId(cidade.getId());
        dto.setNome(cidade.getNome());
        dto.setUf(cidade.getUf());
        return dto;
    }

    private Cidade convertToEntity(CidadeDto dto) {
        Cidade cidade = new Cidade();
        cidade.setNome(dto.getNome());
        cidade.setUf(dto.getUf());
        return cidade;
    }
}

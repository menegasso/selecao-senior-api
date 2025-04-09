package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.EnderecoDto;
import com.selecao.senior.api.entity.Cidade;
import com.selecao.senior.api.entity.Endereco;
import com.selecao.senior.api.exception.ResourceNotFoundException;
import com.selecao.senior.api.repository.CidadeRepository;
import com.selecao.senior.api.repository.EnderecoRepository;
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
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    public List<EnderecoDto> findAll() {
        List<Endereco> enderecos = enderecoRepository.findAll();
        return enderecos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public EnderecoDto findById(Integer id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com id: " + id));
        return convertToDto(endereco);
    }

    public EnderecoDto create(@Valid EnderecoDto dto) {
        try {
            Endereco endereco = convertToEntity(dto);
            Endereco saved = enderecoRepository.save(endereco);
            return convertToDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para criar Endereço.", ex);
        }
    }

    @Transactional
    public EnderecoDto update(Integer id, @Valid EnderecoDto dto) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com id: " + id));
        endereco.setTipoLogradouro(dto.getTipoLogradouro());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setBairro(dto.getBairro());
        // Atualiza a referência para Cidade
        Cidade cidade = cidadeRepository.findById(dto.getCidadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada com id: " + dto.getCidadeId()));
        endereco.setCidade(cidade);
        try {
            Endereco updated = enderecoRepository.save(endereco);
            return convertToDto(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para atualizar Endereço.", ex);
        }
    }

    public void delete(Integer id) {
        if (!enderecoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Endereço não encontrado com id: " + id);
        }
        enderecoRepository.deleteById(id);
    }

    private EnderecoDto convertToDto(Endereco endereco) {
        EnderecoDto dto = new EnderecoDto();
        dto.setId(endereco.getId());
        dto.setTipoLogradouro(endereco.getTipoLogradouro());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setBairro(endereco.getBairro());
        dto.setCidadeId(endereco.getCidade().getId());
        return dto;
    }

    private Endereco convertToEntity(EnderecoDto dto) {
        Endereco endereco = new Endereco();
        endereco.setTipoLogradouro(dto.getTipoLogradouro());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setBairro(dto.getBairro());
        Cidade cidade = cidadeRepository.findById(dto.getCidadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada com id: " + dto.getCidadeId()));
        endereco.setCidade(cidade);
        return endereco;
    }
}

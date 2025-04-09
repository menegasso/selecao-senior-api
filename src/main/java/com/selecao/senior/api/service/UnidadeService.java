package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.EnderecoDto;
import com.selecao.senior.api.dto.UnidadeDto;
import com.selecao.senior.api.entity.Cidade;
import com.selecao.senior.api.entity.Endereco;
import com.selecao.senior.api.entity.Unidade;
import com.selecao.senior.api.repository.UnidadeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class UnidadeService {

    @Autowired
    private UnidadeRepository unidadeRepository;

    public List<UnidadeDto> findAll() {
        List<Unidade> unidades = unidadeRepository.findAll();
        return unidades.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UnidadeDto findById(Integer id) {
        Unidade unidade = unidadeRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
        return convertToDto(unidade);
    }

    public UnidadeDto create(@Valid UnidadeDto unidadeDto) {
        Unidade unidade = convertToEntity(unidadeDto);
        Unidade savedUnidade = unidadeRepository.save(unidade);
        return convertToDto(savedUnidade);
    }

    @Transactional
    public UnidadeDto update(Integer id, @Valid UnidadeDto unidadeDto) {
        Unidade unidade = unidadeRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
        unidade.setNome(unidadeDto.getNome());
        if (unidadeDto.getEnderecos() != null) {
            unidade.setEnderecos(
                    unidadeDto.getEnderecos().stream()
                            .map(this::convertToEnderecoEntity)
                            .collect(Collectors.toList())
            );
        }
        Unidade updatedUnidade = unidadeRepository.save(unidade);
        return convertToDto(updatedUnidade);
    }

    public void delete(Integer id) {
        unidadeRepository.deleteById(Long.valueOf(id));
    }

    private UnidadeDto convertToDto(Unidade unidade) {
        UnidadeDto dto = new UnidadeDto();
        dto.setId(unidade.getId());
        dto.setNome(unidade.getNome());
        dto.setSigla(unidade.getSigla());
        if (unidade.getEnderecos() != null) {
            dto.setEnderecos(
                    unidade.getEnderecos().stream()
                            .map(this::convertToEnderecoDto)
                            .collect(Collectors.toList())
            );
        } else {
            dto.setEnderecos(new ArrayList<>());
        }
        return dto;
    }

    private Unidade convertToEntity(UnidadeDto dto) {
        Unidade unidade = new Unidade();
        unidade.setNome(dto.getNome());
        unidade.setSigla(dto.getSigla());
        if (dto.getEnderecos() != null) {
            unidade.setEnderecos(
                    dto.getEnderecos().stream()
                            .map(this::convertToEnderecoEntity)
                            .collect(Collectors.toList())
            );
        }
        return unidade;
    }

    private EnderecoDto convertToEnderecoDto(Endereco endereco) {
        EnderecoDto dto = new EnderecoDto();
        dto.setId(endereco.getId());
        dto.setTipoLogradouro(endereco.getTipoLogradouro());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setBairro(endereco.getBairro());
        dto.setCidadeId(endereco.getCidade().getId());
        return dto;
    }

    private Endereco convertToEnderecoEntity(EnderecoDto dto) {
        Endereco endereco = new Endereco();
        endereco.setId(dto.getId());
        endereco.setTipoLogradouro(dto.getTipoLogradouro());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setBairro(dto.getBairro());

        Cidade cidade = new Cidade();
        cidade.setId(dto.getCidadeId());
        endereco.setCidade(cidade);
        return endereco;
    }
}

package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.EnderecoDto;
import com.selecao.senior.api.dto.PessoaDto;
import com.selecao.senior.api.entity.Cidade;
import com.selecao.senior.api.entity.Endereco;
import com.selecao.senior.api.entity.Pessoa;
import com.selecao.senior.api.repository.PessoaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Page<PessoaDto> findAll(Pageable pageable) {
        return pessoaRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    public PessoaDto findById(Integer id) {
        Pessoa pessoa = pessoaRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
        return convertToDto(pessoa);
    }

    public PessoaDto create(@Valid PessoaDto pessoaDto) {
        Pessoa pessoa = convertToEntity(pessoaDto);
        Pessoa savedPessoa = pessoaRepository.save(pessoa);
        return convertToDto(savedPessoa);
    }

    @Transactional
    public PessoaDto update(Integer id, @Valid PessoaDto pessoaDto) {
        Pessoa pessoa = pessoaRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
        pessoa.setNome(pessoaDto.getNome());
        pessoa.setDataNascimento(pessoaDto.getDataNascimento());
        pessoa.setSexo(pessoaDto.getSexo());
        pessoa.setMae(pessoaDto.getMae());
        pessoa.setPai(pessoaDto.getPai());
        if (pessoaDto.getEnderecos() != null) {
            pessoa.setEnderecos(
                    pessoaDto.getEnderecos().stream()
                            .map(this::convertToEnderecoEntity)
                            .collect(Collectors.toList())
            );
        }
        Pessoa updatedPessoa = pessoaRepository.save(pessoa);
        return convertToDto(updatedPessoa);
    }

    public void delete(Integer id) {
        pessoaRepository.deleteById(Long.valueOf(id));
    }

    private PessoaDto convertToDto(Pessoa pessoa) {
        PessoaDto dto = new PessoaDto();
        dto.setId(pessoa.getId());
        dto.setNome(pessoa.getNome());
        dto.setDataNascimento(pessoa.getDataNascimento());
        dto.setSexo(pessoa.getSexo());
        dto.setMae(pessoa.getMae());
        dto.setPai(pessoa.getPai());
        if (pessoa.getEnderecos() != null) {
            dto.setEnderecos(
                    pessoa.getEnderecos().stream()
                            .map(this::convertToEnderecoDto)
                            .collect(Collectors.toList())
            );
        } else {
            dto.setEnderecos(new ArrayList<>());
        }
        return dto;
    }

    private Pessoa convertToEntity(PessoaDto dto) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(dto.getNome());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setSexo(dto.getSexo());
        pessoa.setMae(dto.getMae());
        pessoa.setPai(dto.getPai());
        if (dto.getEnderecos() != null) {
            pessoa.setEnderecos(
                    dto.getEnderecos().stream()
                            .map(this::convertToEnderecoEntity)
                            .collect(Collectors.toList())
            );
        }
        return pessoa;
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

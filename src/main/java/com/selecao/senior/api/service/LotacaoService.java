package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.EnderecoDto;
import com.selecao.senior.api.dto.LotacaoDto;
import com.selecao.senior.api.dto.ServidorEfetivoLotacaoDto;
import com.selecao.senior.api.entity.*;
import com.selecao.senior.api.exception.ResourceNotFoundException;
import com.selecao.senior.api.repository.*;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class LotacaoService {

    @Autowired
    private LotacaoRepository lotacaoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private FotoPessoaRepository fotoPessoaRepository;

    @Autowired
    private ServidorEfetivoRepository servidorEfetivoRepository;

    @Autowired
    private MinioService minioService;

    public Page<LotacaoDto> findAll(Pageable pageable) {
        Page<Lotacao> lotacoes = lotacaoRepository.findAll(pageable);
        return lotacoes.map(this::convertToDto);
    }

    public LotacaoDto findById(Long id) {
        Lotacao lotacao = lotacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lotação não encontrada com id: " + id));
        return convertToDto(lotacao);
    }

    public LotacaoDto create(@Valid LotacaoDto dto) {
        try {
            Lotacao lotacao = convertToEntity(dto);
            Lotacao saved = lotacaoRepository.save(lotacao);
            return convertToDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para criar Lotação.", ex);
        }
    }

    @Transactional
    public LotacaoDto update(Long id, @Valid LotacaoDto dto) {
        Lotacao lotacao = lotacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lotação não encontrada com id: " + id));

        lotacao.setDataLotacao(dto.getDataLotacao());
        lotacao.setDataRemocao(dto.getDataRemocao());
        lotacao.setPortaria(dto.getPortaria());

        Unidade unidade = unidadeRepository.findById(dto.getUnidId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com id: " + dto.getUnidId()));
        lotacao.setUnidade(unidade);

        Pessoa pessoa = pessoaRepository.findById((long) dto.getPessoaId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + dto.getPessoaId()));
        lotacao.setPessoa(pessoa);

        try {
            Lotacao updated = lotacaoRepository.save(lotacao);
            return convertToDto(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para atualizar Lotação.", ex);
        }
    }

    public void delete(Long id) {
        if (!lotacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lotação não encontrada com id: " + id);
        }
        lotacaoRepository.deleteById(id);
    }

    private LotacaoDto convertToDto(Lotacao lotacao) {
        LotacaoDto dto = new LotacaoDto();
        dto.setId(lotacao.getId());
        dto.setDataLotacao(lotacao.getDataLotacao());
        dto.setDataRemocao(lotacao.getDataRemocao());
        dto.setPortaria(lotacao.getPortaria());
        dto.setPessoaId(lotacao.getPessoa().getId() != null
                ? lotacao.getPessoa().getId().longValue()
                : null);
        dto.setUnidId(lotacao.getUnidade().getId());
        return dto;
    }
    private Lotacao convertToEntity(LotacaoDto dto) {
        Lotacao lotacao = new Lotacao();
        lotacao.setDataLotacao(dto.getDataLotacao());
        lotacao.setDataRemocao(dto.getDataRemocao());
        lotacao.setPortaria(dto.getPortaria());
        Pessoa pessoa = pessoaRepository.findById((long) dto.getPessoaId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + dto.getPessoaId()));
        lotacao.setPessoa(pessoa);
        Unidade unidade = unidadeRepository.findById(dto.getUnidId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com id: " + dto.getUnidId()));
        lotacao.setUnidade(unidade);

        return lotacao;
    }

    @Transactional(readOnly = true)
    public List<ServidorEfetivoLotacaoDto> getServidoresEfetivosByUnidade(Long unidId) {
        List<Lotacao> lotacoes = lotacaoRepository.findServidoresEfetivosByUnidade(unidId);
        List<ServidorEfetivoLotacaoDto> dtos = new ArrayList<>();

        for (Lotacao lot : lotacoes) {
            if (lot.getPessoa() instanceof ServidorEfetivo) {
                ServidorEfetivo servidor = (ServidorEfetivo) lot.getPessoa();
                int idade = Period.between(servidor.getDataNascimento(), LocalDate.now()).getYears();
                String unidadeNome = lot.getUnidade().getNome();
                String fotoUrl = "";
                var foto = fotoPessoaRepository.findTopByPessoaIdOrderByDataDesc(servidor.getId());
                if (foto != null) {
                    try {
                        // Gera URL temporária com validade de 300 segundos (5 minutos)
                        fotoUrl = minioService.getPresignedUrl(foto.getBucket(), 300);
                    } catch (Exception e) {
                        fotoUrl = "Erro ao gerar URL";
                    }
                }
                dtos.add(new ServidorEfetivoLotacaoDto(servidor.getNome(), idade, unidadeNome, fotoUrl));
            }
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public List<EnderecoDto> getEnderecoFuncionalByNomeParcial(String nomeParcial) {
        // Buscar servidores efetivos cujo nome contenha o trecho informado.
        List<ServidorEfetivo> servidores = servidorEfetivoRepository.findByNomeContainingIgnoreCase(nomeParcial);

        HashSet<EnderecoDto> conjuntoEnderecos = new HashSet<>();

        for (ServidorEfetivo servidor : servidores) {
            Lotacao lotacao = lotacaoRepository.findByPessoaIdAndDataRemocaoIsNull(servidor.getId());
            if (lotacao != null) {
                Unidade unidade = lotacao.getUnidade();
                if (unidade.getEnderecos() != null && !unidade.getEnderecos().isEmpty()) {
                    // Consideramos que o primeiro endereço é o funcional; ajuste se necessário.
                    Endereco enderecoFuncional = unidade.getEnderecos().get(0);
                    EnderecoDto enderecoDto = convertToEnderecoDto(enderecoFuncional);
                    conjuntoEnderecos.add(enderecoDto);
                }
            }
        }
        return new ArrayList<>(conjuntoEnderecos);
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
}

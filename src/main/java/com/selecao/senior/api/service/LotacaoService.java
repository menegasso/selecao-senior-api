package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.LotacaoDto;
import com.selecao.senior.api.entity.Lotacao;
import com.selecao.senior.api.entity.Pessoa;
import com.selecao.senior.api.entity.Unidade;
import com.selecao.senior.api.exception.ResourceNotFoundException;
import com.selecao.senior.api.repository.LotacaoRepository;
import com.selecao.senior.api.repository.PessoaRepository;
import com.selecao.senior.api.repository.UnidadeRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;

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

    public List<LotacaoDto> findAll() {
        List<Lotacao> lotacoes = lotacaoRepository.findAll();
        return lotacoes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
}

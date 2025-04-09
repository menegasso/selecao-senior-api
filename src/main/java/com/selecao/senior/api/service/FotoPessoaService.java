package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.FotoPessoaDto;
import com.selecao.senior.api.entity.FotoPessoa;
import com.selecao.senior.api.entity.Pessoa;
import com.selecao.senior.api.exception.ResourceNotFoundException;
import com.selecao.senior.api.repository.FotoPessoaRepository;
import com.selecao.senior.api.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.InputStream;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.Formatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
public class FotoPessoaService {

    @Autowired
    private FotoPessoaRepository fotoPessoaRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private MinioService minioService;

    public List<FotoPessoaDto> findAll() {
        return fotoPessoaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FotoPessoaDto findById(Integer id) {
        FotoPessoa foto = fotoPessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto não encontrada com id: " + id));
        return convertToDto(foto);
    }

    @Transactional
    public FotoPessoaDto create(@Valid FotoPessoaDto dto) {
        try {
            FotoPessoa foto = convertToEntity(dto);
            foto.setData(LocalDate.now());
            FotoPessoa saved = fotoPessoaRepository.save(foto);
            return convertToDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Dados inválidos para criar FotoPessoa.", ex);
        }
    }

    @Transactional
    public FotoPessoaDto uploadPhoto(MultipartFile file, Long pessoaId) {
        try {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            minioService.uploadFile(file, uniqueFileName);
            String fileHash = generateMD5(file.getInputStream());
            Pessoa pessoa = pessoaRepository.findById(pessoaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + pessoaId));
            FotoPessoa foto = new FotoPessoa();
            foto.setPessoa(pessoa);
            foto.setData(LocalDate.now());
            foto.setBucket(uniqueFileName);
            foto.setHash(fileHash);
            FotoPessoa saved = fotoPessoaRepository.save(foto);
            return convertToDto(saved);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload da foto", e);
        }
    }

    public void delete(Integer id) {
        if (!fotoPessoaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Foto não encontrada com id: " + id);
        }
        fotoPessoaRepository.deleteById(id);
    }

    private String generateMD5(InputStream is) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[1024];
        int numRead;
        while ((numRead = is.read(buffer)) != -1) {
            md.update(buffer, 0, numRead);
        }
        try (Formatter formatter = new Formatter()) {
            for (byte b : md.digest()) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

    private FotoPessoaDto convertToDto(FotoPessoa foto) {
        FotoPessoaDto dto = new FotoPessoaDto();
        dto.setId(foto.getId());
        dto.setData(foto.getData());
        dto.setBucket(foto.getBucket());
        dto.setHash(foto.getHash());
        dto.setPessoaId(foto.getPessoa().getId());
        return dto;
    }

    private FotoPessoa convertToEntity(FotoPessoaDto dto) {
        FotoPessoa foto = new FotoPessoa();
        foto.setBucket(dto.getBucket());
        foto.setHash(dto.getHash());
        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + dto.getPessoaId()));
        foto.setPessoa(pessoa);
        return foto;
    }
}

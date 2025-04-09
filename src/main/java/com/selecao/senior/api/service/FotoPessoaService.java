package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.FotoPessoaDto;
import com.selecao.senior.api.entity.FotoPessoa;
import com.selecao.senior.api.entity.Pessoa;
import com.selecao.senior.api.exception.ResourceNotFoundException;
import com.selecao.senior.api.repository.FotoPessoaRepository;
import com.selecao.senior.api.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.UUID;

@Service
public class FotoPessoaService {

    @Autowired
    private FotoPessoaRepository fotoPessoaRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private MinioService minioService;

    // ----------------- CRUD Básico -----------------

    /**
     * Retorna todas as fotos cadastradas.
     */
    public Page<FotoPessoaDto> findAll(Pageable pageable) {
        Page<FotoPessoa> page = fotoPessoaRepository.findAll(pageable);
        return page.map(this::convertToDto);
    }

    /**
     * Busca uma foto pelo id.
     */
    public FotoPessoaDto findById(Integer id) {
        FotoPessoa foto = fotoPessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto não encontrada com id: " + id));
        return convertToDto(foto);
    }

    /**
     * Cria um registro de FotoPessoa a partir do DTO (não envolve upload).
     */
    @Transactional
    public FotoPessoaDto create(FotoPessoaDto dto) {
        FotoPessoa foto = convertToEntity(dto);
        foto.setData(LocalDate.now());
        FotoPessoa saved = fotoPessoaRepository.save(foto);
        return convertToDto(saved);
    }

    /**
     * Atualiza os metadados de um registro de FotoPessoa.
     */
    @Transactional
    public FotoPessoaDto update(Integer id, FotoPessoaDto dto) {
        FotoPessoa foto = fotoPessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto não encontrada com id: " + id));
        // Atualize os campos que podem ser modificados, se aplicável (por exemplo, somente metadata)
        foto.setBucket(dto.getBucket());
        foto.setHash(dto.getHash());
        // Se necessário atualizar data ou outros campos, inclua aqui.
        FotoPessoa saved = fotoPessoaRepository.save(foto);
        return convertToDto(saved);
    }

    /**
     * Exclui um registro de FotoPessoa pelo id.
     */
    public void delete(Integer id) {
        if (!fotoPessoaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Foto não encontrada com id: " + id);
        }
        fotoPessoaRepository.deleteById(id);
    }

    // ----------------- Upload de Fotos -----------------

    /**
     * Realiza o upload de uma única foto, envia para o MinIO e grava os metadados.
     *
     * @param file     arquivo da foto
     * @param pessoaId ID da pessoa associada à foto
     * @return DTO da foto gravada
     */
    @Transactional
    public FotoPessoaDto uploadPhoto(MultipartFile file, Long pessoaId) {
        try {
            // Gera um nome único para o arquivo
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            System.out.println("Iniciando upload do arquivo: " + uniqueFileName);

            // Envia o arquivo para o MinIO (a URL de conexão deve estar configurada na propriedade minio.url para a porta da API: geralmente 9000)
            minioService.uploadFile(file, uniqueFileName);

            // Gera hash MD5
            String fileHash = generateMD5(file.getInputStream());

            // Busca a Pessoa associada
            Pessoa pessoa = pessoaRepository.findById(pessoaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + pessoaId));

            // Cria e grava o registro da FotoPessoa
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

    /**
     * Realiza o upload de múltiplas fotos.
     *
     * @param files    array de arquivos
     * @param pessoaId ID da pessoa associada às fotos
     * @return lista de DTOs das fotos gravadas
     */
    @Transactional
    public List<FotoPessoaDto> uploadPhotos(MultipartFile[] files, Long pessoaId) {
        List<FotoPessoaDto> dtos = new ArrayList<>();
        for (MultipartFile file : files) {
            dtos.add(uploadPhoto(file, pessoaId));
        }
        return dtos;
    }

    // ----------------- Geração de URL Temporária -----------------

    /**
     * Gera uma URL temporária (presigned) para acesso à foto.
     *
     * @param fotoId        ID da foto
     * @param expirySeconds tempo de expiração da URL em segundos
     * @return URL temporária para acesso
     */
    public String getTemporaryUrlForFoto(Integer fotoId, int expirySeconds) {
        FotoPessoa foto = fotoPessoaRepository.findById(fotoId)
                .orElseThrow(() -> new ResourceNotFoundException("Foto não encontrada com id: " + fotoId));
        try {
            return minioService.getPresignedUrl(foto.getBucket(), expirySeconds);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL temporária da foto", e);
        }
    }

    // ----------------- Métodos Auxiliares -----------------

    /**
     * Converte a entidade FotoPessoa para o DTO correspondente.
     */
    private FotoPessoaDto convertToDto(FotoPessoa foto) {
        FotoPessoaDto dto = new FotoPessoaDto();
        dto.setId(foto.getId());
        dto.setData(foto.getData());
        dto.setBucket(foto.getBucket());
        dto.setHash(foto.getHash());
        if (foto.getPessoa() != null) {
            dto.setPessoaId(foto.getPessoa().getId());
        }
        return dto;
    }

    /**
     * Converte o DTO para a entidade FotoPessoa.
     */
    private FotoPessoa convertToEntity(FotoPessoaDto dto) {
        FotoPessoa foto = new FotoPessoa();
        foto.setBucket(dto.getBucket());
        foto.setHash(dto.getHash());
        // A associação com Pessoa deve ser tratada: neste exemplo, buscamos a Pessoa pelo id
        if (dto.getPessoaId() != null) {
            Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + dto.getPessoaId()));
            foto.setPessoa(pessoa);
        }
        foto.setData(LocalDate.now());
        return foto;
    }

    /**
     * Gera o hash MD5 de um InputStream.
     */
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
}

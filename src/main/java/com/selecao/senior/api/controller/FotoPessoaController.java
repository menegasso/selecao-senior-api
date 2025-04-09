package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.FotoPessoaDto;
import com.selecao.senior.api.service.FotoPessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/fotos-pessoa")
public class FotoPessoaController {

    @Autowired
    private FotoPessoaService fotoPessoaService;

    /**
     * Endpoint para listar todas as fotos cadastradas.
     * GET /api/fotos-pessoa
     */
    @GetMapping
    public ResponseEntity<Page<FotoPessoaDto>> getAllFotos(Pageable pageable) {
        Page<FotoPessoaDto> dtos = fotoPessoaService.findAll(pageable);
        return ResponseEntity.ok(dtos);
    }
    /**
     * Endpoint para buscar uma foto pelo id.
     * GET /api/fotos-pessoa/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<FotoPessoaDto> getFotoById(@PathVariable Integer id) {
        FotoPessoaDto dto = fotoPessoaService.findById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Endpoint para criar um registro de FotoPessoa a partir de um DTO.
     * POST /api/fotos-pessoa
     */
    @PostMapping
    public ResponseEntity<FotoPessoaDto> createFoto(@RequestBody FotoPessoaDto dto) {
        FotoPessoaDto created = fotoPessoaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Endpoint para atualizar um registro de FotoPessoa.
     * PUT /api/fotos-pessoa/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<FotoPessoaDto> updateFoto(@PathVariable Integer id,
                                                    @RequestBody FotoPessoaDto dto) {
        FotoPessoaDto updated = fotoPessoaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Endpoint para excluir um registro de FotoPessoa.
     * DELETE /api/fotos-pessoa/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoto(@PathVariable Integer id) {
        fotoPessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para fazer upload de uma única foto.
     * POST /api/fotos-pessoa/upload
     * Requer multipart/form-data com:
     * - file: arquivo da foto
     * - pessoaId: ID da pessoa associada
     */
    @PostMapping("/upload")
    public ResponseEntity<FotoPessoaDto> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("pessoaId") Long pessoaId) {
        FotoPessoaDto dto = fotoPessoaService.uploadPhoto(file, pessoaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Endpoint para fazer upload de múltiplas fotos.
     * POST /api/fotos-pessoa/uploadMany
     * Requer multipart/form-data com:
     * - files: array de arquivos
     * - pessoaId: ID da pessoa associada
     */
    @PostMapping("/uploadMany")
    public ResponseEntity<List<FotoPessoaDto>> uploadMultiplePhotos(@RequestParam("files") MultipartFile[] files,
                                                                    @RequestParam("pessoaId") Long pessoaId) {
        List<FotoPessoaDto> dtos = fotoPessoaService.uploadPhotos(files, pessoaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }

    /**
     * Endpoint para gerar uma URL temporária para acesso à foto.
     * GET /api/fotos-pessoa/{id}/url
     * Opcionalmente, pode-se passar o parâmetro query "expiry" para definir o tempo em segundos.
     * Exemplo: /api/fotos-pessoa/1/url?expiry=300
     */
    @GetMapping("/{id}/url")
    public ResponseEntity<String> getTemporaryUrl(@PathVariable Integer id,
                                                  @RequestParam(value = "expiry", defaultValue = "300") int expiry) {
        String url = fotoPessoaService.getTemporaryUrlForFoto(id, expiry);
        return ResponseEntity.ok(url);
    }
}

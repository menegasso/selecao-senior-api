package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.FotoPessoaDto;
import com.selecao.senior.api.service.FotoPessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public ResponseEntity<List<FotoPessoaDto>> getAll() {
        return ResponseEntity.ok(fotoPessoaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoPessoaDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(fotoPessoaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FotoPessoaDto> create(@Valid @RequestBody FotoPessoaDto dto) {
        if (dto.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(fotoPessoaService.create(dto));
    }

    @PostMapping("/upload")
    public ResponseEntity<FotoPessoaDto> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("pessoaId") Long pessoaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fotoPessoaService.uploadPhoto(file, pessoaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        fotoPessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

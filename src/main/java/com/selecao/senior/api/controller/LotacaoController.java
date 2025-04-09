package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.LotacaoDto;
import com.selecao.senior.api.service.LotacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/lotacoes")
public class LotacaoController {

    @Autowired
    private LotacaoService lotacaoService;

    @GetMapping
    public ResponseEntity<List<LotacaoDto>> getAll() {
        return ResponseEntity.ok(lotacaoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotacaoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(lotacaoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LotacaoDto> create(@Valid @RequestBody LotacaoDto dto) {
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O campo id não deve ser enviado na criação.");
        }
        LotacaoDto created = lotacaoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LotacaoDto> update(@PathVariable Long id, @Valid @RequestBody LotacaoDto dto) {
        if (dto.getId() != null && !dto.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O campo id não deve ser enviado ou deve coincidir com o id da URL.");
        }
        LotacaoDto updated = lotacaoService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lotacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

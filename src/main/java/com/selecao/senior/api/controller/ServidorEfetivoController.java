package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.ServidorEfetivoDto;
import com.selecao.senior.api.service.ServidorEfetivoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/servidores-efetivos")
public class ServidorEfetivoController {

    @Autowired
    private ServidorEfetivoService service;

    @GetMapping
    public ResponseEntity<Page<ServidorEfetivoDto>> getAll(Pageable pageable) {
        Page<ServidorEfetivoDto> page = service.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServidorEfetivoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ServidorEfetivoDto> create(@Valid @RequestBody ServidorEfetivoDto dto) {
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo id não deve ser enviado na criação.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorEfetivoDto> update(@PathVariable Long id, @Valid @RequestBody ServidorEfetivoDto dto) {
        if (dto.getId() != null && !dto.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo id não deve ser enviado ou deve coincidir com o id da URL.");
        }
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

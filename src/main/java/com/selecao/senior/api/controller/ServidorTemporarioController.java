package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.ServidorTemporarioDto;
import com.selecao.senior.api.service.ServidorTemporarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/servidores-temporarios")
public class ServidorTemporarioController {

    @Autowired
    private ServidorTemporarioService service;

    @GetMapping
    public ResponseEntity<List<ServidorTemporarioDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServidorTemporarioDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ServidorTemporarioDto> create(@Valid @RequestBody ServidorTemporarioDto dto) {
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo id não deve ser enviado na criação.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorTemporarioDto> update(@PathVariable Long id, @Valid @RequestBody ServidorTemporarioDto dto) {
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

package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.CidadeDto;
import com.selecao.senior.api.service.CidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/cidades")
public class CidadeController {

    @Autowired
    private CidadeService cidadeService;

    @GetMapping
    public ResponseEntity<List<CidadeDto>> getAll() {
        return ResponseEntity.ok(cidadeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadeDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cidadeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CidadeDto> create(@Valid @RequestBody CidadeDto cidadeDto) {
        if (cidadeDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O campo id não deve ser enviado na criação.");
        }
        CidadeDto created = cidadeService.create(cidadeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadeDto> update(@PathVariable Long id, @Valid @RequestBody CidadeDto cidadeDto) {
        if (cidadeDto.getId() != null && !cidadeDto.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O campo id não deve ser enviado ou deve coincidir com o id da URL.");
        }
        CidadeDto updated = cidadeService.update(id, cidadeDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

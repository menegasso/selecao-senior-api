package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.UnidadeDto;
import com.selecao.senior.api.service.UnidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeService unidadeService;

    @GetMapping
    public ResponseEntity<List<UnidadeDto>> getAll() {
        return ResponseEntity.ok(unidadeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(unidadeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UnidadeDto> create(@Valid @RequestBody UnidadeDto unidadeDto) {
        if (unidadeDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O campo id não deve ser enviado na criação.");
        }
        UnidadeDto created = unidadeService.create(unidadeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeDto> update(@PathVariable Integer id, @Valid @RequestBody UnidadeDto unidadeDto) {
        if (unidadeDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O campo id não deve ser enviado ou deve coincidir com o id da URL.");
        }
        UnidadeDto updated = unidadeService.update(id, unidadeDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        unidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

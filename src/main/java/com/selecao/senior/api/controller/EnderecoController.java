package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.EnderecoDto;
import com.selecao.senior.api.service.EnderecoService;
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
@RequestMapping("/api/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<Page<EnderecoDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(enderecoService.findAll(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(enderecoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EnderecoDto> create(@Valid @RequestBody EnderecoDto dto) {
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo id não deve ser enviado na criação.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoDto> update(@PathVariable Integer id, @Valid @RequestBody EnderecoDto dto) {
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo id não deve ser enviado ou deve coincidir com o id da URL.");
        }
        return ResponseEntity.ok(enderecoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        enderecoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

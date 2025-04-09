package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.PessoaDto;
import com.selecao.senior.api.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<Page<PessoaDto>> getAll(Pageable pageable) {
        Page<PessoaDto> page = pessoaService.findAll(pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PessoaDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(pessoaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PessoaDto> create(@Valid @RequestBody PessoaDto pessoaDto) {
        if (pessoaDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O campo id não deve ser enviado na criação.");
        }
        PessoaDto created = pessoaService.create(pessoaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaDto> update(@PathVariable Integer id, @Valid @RequestBody PessoaDto pessoaDto) {
        if (pessoaDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O campo id não deve ser enviado ou deve coincidir com o id da URL.");
        }
        PessoaDto updated = pessoaService.update(id, pessoaDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

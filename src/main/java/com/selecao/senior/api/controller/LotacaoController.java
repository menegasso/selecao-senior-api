package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.EnderecoDto;
import com.selecao.senior.api.dto.LotacaoDto;
import com.selecao.senior.api.dto.ServidorEfetivoLotacaoDto;
import com.selecao.senior.api.service.LotacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lotacoes")
public class LotacaoController {

    @Autowired
    private LotacaoService lotacaoService;

    @GetMapping
    public ResponseEntity<Page<LotacaoDto>> getAll(Pageable pageable) {
        Page<LotacaoDto> page = lotacaoService.findAll(pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LotacaoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(lotacaoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LotacaoDto> create(@Valid @RequestBody LotacaoDto dto) {
        if (dto.getId() != null) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O campo id não deve ser enviado na criação.");
        }
        LotacaoDto created = lotacaoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LotacaoDto> update(@PathVariable Long id, @Valid @RequestBody LotacaoDto dto) {
        if (dto.getId() != null && !dto.getId().equals(id)) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,
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

    @GetMapping("/servidores-efetivos")
    public ResponseEntity<List<ServidorEfetivoLotacaoDto>> getServidoresEfetivosByUnidade(@RequestParam("unid_id") Long unidId) {
        List<ServidorEfetivoLotacaoDto> dtos = lotacaoService.getServidoresEfetivosByUnidade(unidId);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/endereco-funcional")
    public ResponseEntity<List<EnderecoDto>> getEnderecoFuncionalByNomeParcial(@RequestParam("nomeParcial") String nomeParcial) {
        List<EnderecoDto> enderecos = lotacaoService.getEnderecoFuncionalByNomeParcial(nomeParcial);
        return ResponseEntity.ok(enderecos);
    }
}

package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.RoleDto;
import com.selecao.senior.api.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleDto> create(@Valid @RequestBody RoleDto roleDto) {
        if (roleDto.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        RoleDto created = roleService.create(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> update(@PathVariable Long id, @Valid @RequestBody RoleDto roleDto) {
        if (roleDto.getId() != null && !roleDto.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        RoleDto updated = roleService.update(id, roleDto);
        return ResponseEntity.ok(updated);
    }

}

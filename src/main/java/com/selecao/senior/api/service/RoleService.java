package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.RoleDto;
import com.selecao.senior.api.entity.Role;
import com.selecao.senior.api.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RoleDto findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));
        return convertToDto(role);
    }

    public RoleDto create(@Valid RoleDto roleDto) {
        Role role = convertToEntity(roleDto);
        Role saved = roleRepository.save(role);
        return convertToDto(saved);
    }

    public RoleDto update(Long id, @Valid RoleDto roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));
        role.setNome(roleDto.getNome());
        Role updated = roleRepository.save(role);
        return convertToDto(updated);
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    private RoleDto convertToDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setNome(role.getNome());
        return dto;
    }

    private Role convertToEntity(RoleDto dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setNome(dto.getNome());
        return role;
    }
}

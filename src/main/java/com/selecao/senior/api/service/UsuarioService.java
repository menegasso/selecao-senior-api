package com.selecao.senior.api.service;

import com.selecao.senior.api.dto.RoleDto;
import com.selecao.senior.api.dto.UsuarioDto;
import com.selecao.senior.api.entity.Role;
import com.selecao.senior.api.entity.Usuario;
import com.selecao.senior.api.repository.RoleRepository;
import com.selecao.senior.api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UsuarioDto> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UsuarioDto findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return convertToDto(usuario);
    }

    public UsuarioDto create(@Valid UsuarioDto usuarioDto) {
        usuarioDto.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
        Usuario usuario = convertToEntity(usuarioDto);
        Usuario saved = usuarioRepository.save(usuario);
        return convertToDto(saved);
    }

    @Transactional
    public UsuarioDto update(Long id, @Valid UsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setUsername(usuarioDto.getUsername());
        if (usuarioDto.getSenha() != null && !usuarioDto.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
        }
        if (usuarioDto.getRoles() != null) {
            List<Role> roles = usuarioDto.getRoles().stream()
                    .map(this::convertRoleToEntity)
                    .collect(Collectors.toList());
            usuario.setRoles(roles);
        }
        Usuario updated = usuarioRepository.save(usuario);
        return convertToDto(updated);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDto convertToDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setSenha(null);
        if (usuario.getRoles() != null) {
            dto.setRoles(usuario.getRoles().stream()
                    .map(this::convertRoleToDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setRoles(Collections.emptyList());
        }
        return dto;
    }

    private Usuario convertToEntity(UsuarioDto dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setSenha(dto.getSenha());
        if (dto.getRoles() != null) {
            List<Role> roles = dto.getRoles().stream()
                    .map(this::convertRoleToEntity)
                    .collect(Collectors.toList());
            usuario.setRoles(roles);
        }
        return usuario;
    }

    private RoleDto convertRoleToDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setNome(role.getNome());
        return dto;
    }

    private Role convertRoleToEntity(RoleDto dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setNome(dto.getNome());
        return role;
    }
}

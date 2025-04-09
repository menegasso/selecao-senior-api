package com.selecao.senior.api.config;

import com.selecao.senior.api.entity.Role;
import com.selecao.senior.api.entity.Usuario;
import com.selecao.senior.api.repository.RoleRepository;
import com.selecao.senior.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            Role roleUser = new Role();
            roleUser.setNome("ROLE_USER");
            roleUser = roleRepository.save(roleUser);

            Usuario usuario = new Usuario();
            usuario.setUsername("admin");
            usuario.setSenha(passwordEncoder.encode("admin123"));
            usuario.setRoles(Collections.singletonList(roleUser));

            usuarioRepository.save(usuario);
        }
    }
}

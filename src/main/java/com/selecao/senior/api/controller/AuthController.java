package com.selecao.senior.api.controller;

import com.selecao.senior.api.dto.AuthResponse;
import com.selecao.senior.api.dto.LoginRequest;
import com.selecao.senior.api.config.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        String jwt = tokenProvider.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(new AuthResponse(jwt, "Bearer"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);
        if (tokenProvider.validateToken(token)) {
            String username = tokenProvider.getUsernameFromJWT(token);
            String newToken = tokenProvider.generateToken(username);
            AuthResponse response = new AuthResponse(newToken, "Bearer");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
    }
}

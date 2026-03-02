package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.LoginRequest; // Створи цей DTO (username, password)
import org.example.security.JwtCore;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        // Спроба аутентифікації (Spring сам перевірить пароль)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Якщо все ок, кладемо в контекст
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Генеруємо токен
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtCore.generateToken(userDetails);

        return ResponseEntity.ok(jwt); // Повертаємо токен клієнту
    }
}
package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public User getCurrentUser() {
        // Отримуємо аутентифікацію
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Тепер ми можемо безпечно кастити до нашого класу
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Шукаємо юзера в базі по ID (це дуже швидко)
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // Читаємо список з файлу налаштувань
    @Value("#{'${app.security.admins}'.split(',')}")
    private List<String> admins;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // ЛОГІКА РОЛЕЙ БЕЗ БАЗИ ДАНИХ
        // За замовчуванням роль USER
        String role = "ROLE_USER";

        // Якщо цей юзер у списку адмінів -> даємо йому роль ADMIN
        if (admins.contains(username)) {
            role = "ROLE_ADMIN";
        }

        // Створюємо список прав (Authority)
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        // Передаємо ці права в UserDetailsImpl
        return UserDetailsImpl.build(user, authorities);
    }
}
package org.example.repository;

import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindById() {
        User user = User.builder()
                .email("test@example.com")
                .username("tester")
                .passwordHash("hash")
                .createdAt(LocalDateTime.now())
                .status(User.Status.active)
                .build();

        User saved = userRepository.save(user);
        assertThat(saved.getId()).isNotNull();

        User found = userRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@example.com");
    }
}

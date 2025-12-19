package org.example.service;

import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.repository.UserRepository;
import org.example.service.impl.UserServiceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UserServiceimplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceimpl userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceimpl(userRepository, passwordEncoder);
    }

    @Test
    void createShouldSaveUser() {
        UserRequest req = new UserRequest(null, "a@b.com", "user", "secret", null);
        when(userRepository.save(any())).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponse resp = userService.create(req);
        assertThat(resp.getId()).isEqualTo(1L);
        verify(userRepository).save(any());
    }

    @Test
    void getByIdNotFoundShouldThrow() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getById(5L)).isInstanceOf(UserNotFoundException.class);
    }
}


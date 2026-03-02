package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.example.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceimpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthService userHelper; // Додали для перевірки прав

    @Override
    public UserResponse create(UserRequest request) {
        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .passwordHash(passwordEncoder.encode(request.password()))
                .createdAt(LocalDateTime.now())
                .status(User.Status.active)
                // .role(Role.USER) <-- ПРИБРАЛИ, бо ролі в базі немає
                .build();

        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long id, org.example.dto.request.UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        User currentUser = userHelper.getCurrentUser();
        // Редагувати профіль може тільки сам користувач
        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("Ви не можете редагувати профіль іншого користувача");
        }

        // update fields
        if (request.password() != null) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        UserMapper.updateEntity(user, request);

        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        User currentUser = userHelper.getCurrentUser();
        boolean isSelfDelete = currentUser.getId().equals(id);
        boolean isAdmin = userHelper.isAdmin(); // Перевірка через конфіг (все працює)

        // Видалити може: Сам користувач АБО Адмін
        if (!isSelfDelete && !isAdmin) {
            throw new AccessDeniedException("Ви не маєте права видалити цього користувача");
        }

        // Прибрали перевірку targetUser.getRole(), бо в базі немає такого поля.
        // Якщо треба захистити адміна від видалення іншим адміном,
        // це треба робити перевіркою username через список адмінів,
        // але для початку достатньо поточної перевірки.

        userRepository.deleteById(id);
    }
}
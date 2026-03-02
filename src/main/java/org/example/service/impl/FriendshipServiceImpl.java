package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.FriendshipRequest;
import org.example.dto.request.FriendshipUpdateRequest;
import org.example.dto.response.FriendshipResponse;
import org.example.entity.Friendship;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.FriendshipMapper;
import org.example.repository.FriendshipRepository;
import org.example.service.AuthService;
import org.example.service.FriendshipService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository repo;
    private final AuthService userHelper;

    @Override
    public FriendshipResponse create(FriendshipRequest request) {
        Friendship f = FriendshipMapper.toEntity(request);
        // Автоматично встановлюємо, що підписник - це поточний юзер
        if (f.getFollower() == null) {
            f.setFollower(userHelper.getCurrentUser());
        }
        Friendship saved = repo.save(f);
        return FriendshipMapper.toResponse(saved);
    }

    @Override
    public List<FriendshipResponse> getAll() {
        return repo.findAll().stream().map(FriendshipMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public FriendshipResponse getById(Long id) {
        Friendship f = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Friendship", id));
        return FriendshipMapper.toResponse(f);
    }

    @Override
    public FriendshipResponse update(Long id, FriendshipUpdateRequest request) {
        Friendship f = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Friendship", id));

        // Перевірка прав на редагування (якщо таке передбачено)
        checkDeletePermissions(f);

        FriendshipMapper.updateEntity(f, request);
        Friendship saved = repo.save(f);
        return FriendshipMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Friendship f = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Friendship", id));

        // Викликаємо нашу нову сувору перевірку
        checkDeletePermissions(f);

        repo.deleteById(id);
    }

    // --- Приватний метод перевірки прав ---
    private void checkDeletePermissions(Friendship f) {
        User currentUser = userHelper.getCurrentUser();
        Long currentUserId = currentUser.getId();

        // Отримуємо ID підписника (хто ініціював дружбу)
        Long followerId = (f.getFollower() != null) ? f.getFollower().getId() : -1L;

        // Перевіряємо права:
        // 1. Це я підписався (я follower)?
        boolean isFollower = currentUserId.equals(followerId);
        // 2. Я Адмін?
        boolean isAdmin = userHelper.isAdmin();

        // Якщо я не підписник і не адмін (навіть якщо я той, на кого підписані) -> ЗАБОРОНА
        if (!isFollower && !isAdmin) {
            throw new AccessDeniedException("Ви не можете видалити цю підписку (відписатися може тільки автор підписки)");
        }
    }

    // --- nested resource methods
    @Override
    public List<FriendshipResponse> getFollowersForUser(Long userId) {
        return repo.findByFolloweeId(userId).stream().map(FriendshipMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FriendshipResponse> getFolloweesForUser(Long userId) {
        return repo.findByFollowerId(userId).stream().map(FriendshipMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public FriendshipResponse getByIdAndFolloweeId(Long id, Long followeeId) {
        Friendship f = repo.findByIdAndFolloweeId(id, followeeId).orElseThrow(() -> new EntityNotFoundException("Friendship", id));
        return FriendshipMapper.toResponse(f);
    }

    @Override
    public FriendshipResponse getByIdAndFollowerId(Long id, Long followerId) {
        Friendship f = repo.findByIdAndFollowerId(id, followerId).orElseThrow(() -> new EntityNotFoundException("Friendship", id));
        return FriendshipMapper.toResponse(f);
    }

    @Override
    public FriendshipResponse createForFollower(Long followerId, FriendshipRequest request) {
        // Тут логіка трохи специфічна: створюємо дружбу де followerId є підписником
        User currentUser = userHelper.getCurrentUser();

        // Перевіряємо: чи поточний юзер і є тим followerId, за якого ми намагаємось підписатися
        if (!currentUser.getId().equals(followerId) && !userHelper.isAdmin()) {
            throw new AccessDeniedException("Ви не можете підписуватися від імені іншого користувача");
        }

        Friendship reqEntity = FriendshipMapper.toEntity(request);
        if (reqEntity.getFollower() == null) {
            User u = new User(); u.setId(followerId); reqEntity.setFollower(u);
        }
        Friendship saved = repo.save(reqEntity);
        return FriendshipMapper.toResponse(saved);
    }

    @Override
    public void deleteForUser(Long userId, Long friendshipId) {
        Friendship f = repo.findById(friendshipId).orElseThrow(() -> new EntityNotFoundException("Friendship", friendshipId));

        // Перевіряємо права доступу через той самий метод
        checkDeletePermissions(f);

        repo.delete(f);
    }

    @Override
    public FriendshipResponse createForUser(Long userId, FriendshipRequest request) {
        // Створюємо підписку, де userId виступає ініціатором (follower)
        User currentUser = userHelper.getCurrentUser();
        if (!currentUser.getId().equals(userId) && !userHelper.isAdmin()) {
            throw new AccessDeniedException("Не можна створювати підписку за іншого");
        }

        Friendship reqEntity = FriendshipMapper.toEntity(request);
        if (reqEntity.getFollower() == null) { User u = new User(); u.setId(userId); reqEntity.setFollower(u); }
        Friendship saved = repo.save(reqEntity);
        return FriendshipMapper.toResponse(saved);
    }

    @Override
    public FriendshipResponse updateForUser(Long userId, Long friendshipId, FriendshipUpdateRequest request) {
        Friendship f = repo.findById(friendshipId).orElseThrow(() -> new EntityNotFoundException("Friendship", friendshipId));

        checkDeletePermissions(f); // Тільки підписник може щось міняти

        FriendshipMapper.updateEntity(f, request);
        Friendship saved = repo.save(f);
        return FriendshipMapper.toResponse(saved);
    }

    @Override
    public List<FriendshipResponse> getByUserId(Long userId) {
        return repo.findByFollowerIdOrFolloweeId(userId, userId).stream().map(FriendshipMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public FriendshipResponse getByUserIdAndFriendshipId(Long userId, Long friendshipId) {
        Friendship f = repo.findById(friendshipId).orElseThrow(() -> new EntityNotFoundException("Friendship", friendshipId));
        return FriendshipMapper.toResponse(f);
    }
}
# API Endpoints (Postogram)

Документ містить всі REST-ендпоінти, які реалізовані в контролерах проєкту.

Формат: METHOD PATH — короткий опис
  - Request body: DTO (якщо є)
  - Response: DTO / статус
  - HATEOAS: які посилання додаються (self, collection, relation)

---

## Users (/api/users)

- POST /api/users — створити користувача
  - Request body: UserRequest
  - Response: UserResponse (201 Created)
  - HATEOAS: self (/api/users/{id}), users (collection /api/users)

- GET /api/users — список користувачів
  - Request body: —
  - Response: Collection<UserResponse> (200 OK)
  - HATEOAS: self (/api/users)

- GET /api/users/{id} — отримати користувача за id
  - Request body: —
  - Response: UserResponse (200 OK)
  - HATEOAS: self (/api/users/{id}), users (/api/users), posts (/api/users/{id}/posts)

- PUT /api/users/{id} — оновити користувача
  - Request body: UserUpdateRequest
  - Response: UserResponse (200 OK)
  - HATEOAS: self, users

- DELETE /api/users/{id} — видалити користувача
  - Response: 204 No Content

### Вкладені ресурси: пости користувача

- GET /api/users/{userId}/posts — список постів конкретного користувача
  - Response: Collection<PostResponse> (200 OK)
  - HATEOAS: self (/api/users/{userId}/posts), кожен пост має self (/api/users/{userId}/posts/{postId})

- GET /api/users/{userId}/posts/{postId} — отримати конкретний пост користувача
  - Response: PostResponse (200 OK)
  - HATEOAS: self, posts (collection for user), user (/api/users/{userId})

- POST /api/users/{userId}/posts — створити пост для користувача
  - Request body: PostRequest
  - Response: PostResponse (201 Created)
  - HATEOAS: self (/api/users/{userId}/posts/{id}), posts

- PUT /api/users/{userId}/posts/{postId} — оновити пост користувача
  - Request body: PostUpdateRequest
  - Response: PostResponse (200 OK)

- DELETE /api/users/{userId}/posts/{postId} — видалити пост користувача
  - Response: 204 No Content

---

## Posts (/api/posts)

- POST /api/posts — створити пост
  - Request body: PostRequest
  - Response: PostResponse (201 Created)
  - HATEOAS: self (/api/posts/{id}), posts (/api/posts)

- GET /api/posts — список постів
  - Response: Collection<PostResponse> (200 OK)

- GET /api/posts/{id} — отримати пост за id
  - Response: PostResponse (200 OK)

- PUT /api/posts/{id} — оновити пост
  - Request body: PostUpdateRequest
  - Response: PostResponse (200 OK)

- DELETE /api/posts/{id} — видалити пост
  - Response: 204 No Content

### Вкладені ресурси: коментарі (/api/posts/{postId}/comments)

- GET /api/posts/{postId}/comments — список коментарів для поста
  - Response: Collection<CommentResponse> (200 OK)

- GET /api/posts/{postId}/comments/{commentId} — отримати конкретний коментар для поста
  - Response: CommentResponse (200 OK)

- POST /api/posts/{postId}/comments — створити коментар для поста
  - Request body: CommentRequest
  - Response: CommentResponse (201 Created)

- PUT /api/posts/{postId}/comments/{commentId} — оновити коментар
  - Request body: CommentUpdateRequest
  - Response: CommentResponse (200 OK)

- DELETE /api/posts/{postId}/comments/{commentId} — видалити коментар
  - Response: 204 No Content

### Вкладені ресурси: лайки (/api/posts/{postId}/likes)

- GET /api/posts/{postId}/likes — список лайків для поста
  - Response: Collection<LikesResponse> (200 OK)

- GET /api/posts/{postId}/likes/{likeId} — конкретний запис лайка
  - Response: LikesResponse (200 OK)

- POST /api/posts/{postId}/likes — створити лайк для поста
  - Request body: LikesRequest
  - Response: LikesResponse (201 Created)

- PUT /api/posts/{postId}/likes/{likeId} — оновити лайк
  - Request body: LikesUpdateRequest
  - Response: LikesResponse (200 OK)

- DELETE /api/posts/{postId}/likes/{likeId} — видалити лайк
  - Response: 204 No Content

### Вкладені ресурси: медіа-ресурси (/api/posts/{postId}/media-assets)

- GET /api/posts/{postId}/media-assets — список медіа для поста
  - Response: Collection<MediaAssetResponse> (200 OK)

- GET /api/posts/{postId}/media-assets/{mediaId} — конкретне медіа
  - Response: MediaAssetResponse (200 OK)

- POST /api/posts/{postId}/media-assets — додати медіа до поста
  - Request body: MediaAssetRequest
  - Response: MediaAssetResponse (201 Created)

- PUT /api/posts/{postId}/media-assets/{mediaId} — оновити медіа
  - Request body: MediaAssetUpdateRequest
  - Response: MediaAssetResponse (200 OK)

- DELETE /api/posts/{postId}/media-assets/{mediaId} — видалити медіа
  - Response: 204 No Content

---

## Comments (/api/comments)

- POST /api/comments — створити коментар (без контексту поста)
  - Request body: CommentRequest
  - Response: CommentResponse (201 Created)

- GET /api/comments — список всіх коментарів
  - Response: List<CommentResponse> (200 OK)

- GET /api/comments/{id} — отримати коментар
  - Response: CommentResponse (200 OK)

- PUT /api/comments/{id} — оновити коментар
  - Request body: CommentUpdateRequest
  - Response: CommentResponse (200 OK)

- DELETE /api/comments/{id} — видалити коментар
  - Response: 204 No Content

### Вкладені під /api/comments/posts (альтернативні шляхи)

- GET /api/comments/posts/{postId} — список коментарів для поста
- GET /api/comments/posts/{postId}/{commentId} — конкретний коментар для поста
- POST /api/comments/posts/{postId} — створити коментар для поста
- PUT /api/comments/posts/{postId}/{commentId} — оновити
- DELETE /api/comments/posts/{postId}/{commentId} — видалити

(Контролер підтримує вкладеність під `/api/comments/posts/...` як альтернативу `/api/posts/{postId}/comments`.)

---

## Likes (/api/likes)

- POST /api/likes — створити лайк (без контексту поста)
  - Request body: LikesRequest
  - Response: LikesResponse (201 Created)

- GET /api/likes — список всіх лайків
  - Response: List<LikesResponse> (200 OK)

- GET /api/likes/{id} — отримати лайк
  - Response: LikesResponse (200 OK)

- PUT /api/likes/{id} — оновити лайк
  - Request body: LikesUpdateRequest
  - Response: LikesResponse (200 OK)

- DELETE /api/likes/{id} — видалити лайк
  - Response: 204 No Content

### Вкладені під /api/likes/posts

- GET /api/likes/posts/{postId} — список лайків для поста
- GET /api/likes/posts/{postId}/{likeId} — конкретний лайк для поста
- POST /api/likes/posts/{postId} — створити лайк для поста
- PUT /api/likes/posts/{postId}/{likeId} — оновити
- DELETE /api/likes/posts/{postId}/{likeId} — видалити

(Альтернатива до `/api/posts/{postId}/likes` — обидва набори маршрутів доступні в контролерах.)

---

## Media Assets (/api/media-assets)

- POST /api/media-assets — створити медіа (без контексту поста)
  - Request body: MediaAssetRequest
  - Response: MediaAssetResponse (201 Created)

- GET /api/media-assets — список всіх медіа
  - Response: List<MediaAssetResponse> (200 OK)

- GET /api/media-assets/{id} — отримати медіа
  - Response: MediaAssetResponse (200 OK)

- PUT /api/media-assets/{id} — оновити медіа
  - Request body: MediaAssetUpdateRequest
  - Response: MediaAssetResponse (200 OK)

- DELETE /api/media-assets/{id} — видалити медіа
  - Response: 204 No Content

### Вкладені під /api/media-assets/posts

- GET /api/media-assets/posts/{postId} — список медіа для поста
- GET /api/media-assets/posts/{postId}/{mediaId} — конкретне медіа
- POST /api/media-assets/posts/{postId} — створити для поста
- PUT /api/media-assets/posts/{postId}/{mediaId} — оновити
- DELETE /api/media-assets/posts/{postId}/{mediaId} — видалити

(Альтернатива до `/api/posts/{postId}/media-assets`.)

---

## Friendships (/api/friendships)

- POST /api/friendships — створити дружбу/підписку
  - Request body: FriendshipRequest
  - Response: FriendshipResponse (201 Created)

- GET /api/friendships — список всіх дружб
  - Response: List<FriendshipResponse> (200 OK)

- GET /api/friendships/{id} — отримати дружбу
  - Response: FriendshipResponse (200 OK)

- PUT /api/friendships/{id} — оновити
  - Request body: FriendshipUpdateRequest
  - Response: FriendshipResponse (200 OK)

- DELETE /api/friendships/{id} — видалити дружбу
  - Response: 204 No Content

### Вкладені під /api/friendships/users — перегляд підписок/фоловерів користувача

- GET /api/friendships/users/{userId}/followers — список follower-ів (хто підписаний на userId)
- GET /api/friendships/users/{userId}/followers/{friendshipId} — конкретний запис фоловера
- GET /api/friendships/users/{userId}/followees — список followee-ів (кого userId підписаний)
- GET /api/friendships/users/{userId}/followees/{friendshipId} — конкретний запис followee
- POST /api/friendships/users/{userId}/followees — створити підписку (userId -> followee в request)
- DELETE /api/friendships/users/{userId}/{friendshipId} — видалити запис дружби

---

## Profiles (/api/profiles)

- POST /api/profiles — створити профіль
  - Request body: ProfileRequest
  - Response: ProfileResponse (201 Created)

- GET /api/profiles — список профілів
  - Response: List<ProfileResponse> (200 OK)

- GET /api/profiles/{userId} — отримати профіль по userId
  - Response: ProfileResponse (200 OK)

- PUT /api/profiles/{userId} — оновити профіль
  - Request body: ProfileUpdateRequest
  - Response: ProfileResponse (200 OK)

- DELETE /api/profiles/{userId} — видалити профіль
  - Response: 204 No Content

### Вкладені під /api/profiles/users

- GET /api/profiles/users/{userId} — отримати профіль користувача (HATEOAS wrapper)
- POST /api/profiles/users/{userId} — створити профіль для користувача
- PUT /api/profiles/users/{userId} — оновити профіль користувача
- DELETE /api/profiles/users/{userId} — видалити профіль користувача

---

## Stories (/api/stories)

- POST /api/stories — створити story
  - Request body: StoriesRequest
  - Response: StoriesResponse (201 Created)

- GET /api/stories — список stories
  - Response: List<StoriesResponse> (200 OK)

- GET /api/stories/{id} — отримати story
  - Response: StoriesResponse (200 OK)

- PUT /api/stories/{id} — оновити
  - Request body: StoriesUpdateRequest
  - Response: StoriesResponse (200 OK)

- DELETE /api/stories/{id} — видалити
  - Response: 204 No Content

### Вкладені під /api/stories/users

- GET /api/stories/users/{userId} — список stories конкретного користувача
- GET /api/stories/users/{userId}/{storyId} — отримати story користувача
- POST /api/stories/users/{userId} — створити story для користувача
- PUT /api/stories/users/{userId}/{storyId} — оновити
- DELETE /api/stories/users/{userId}/{storyId} — видалити

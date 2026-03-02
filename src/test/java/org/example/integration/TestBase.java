package org.example.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.dto.request.PostRequest;
import org.example.dto.request.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

public class TestBase {

    @LocalServerPort
    int port;

    protected static final String USERS = "/api/users";
    protected static final String POSTS = "/api/posts";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    protected Long createUser(String email, String username, String password) {
        UserRequest req = new UserRequest(null, email, username, password, null);
        return RestAssured.given()
                .basePath("") // ensure this request ignores any global basePath set by tests
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post(USERS)
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    protected Long createPost(Long userId, String caption, String location) {
        PostRequest req = new PostRequest(null, userId, caption, location);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post(POSTS)
                .then()
                .statusCode(201)
                .extract().path("id");
    }
}

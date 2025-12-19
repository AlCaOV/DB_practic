package org.example.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.dto.request.UserRequest;
import org.example.dto.request.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerRestAssuredTest extends TestBase {

    private static final String BASE_PATH = "/api/users";

    @BeforeEach
    void init() {
        RestAssured.basePath = BASE_PATH;
    }

    @Test
    void crudFlowRestAssured() {
        // Create
        UserRequest req = new UserRequest(null, "restassured@example.com", "rauser", "password123", null);

        Integer id = given()
                .contentType(ContentType.JSON)
                .body(req)
        .when()
                .post()
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("email", equalTo("restassured@example.com"))
                .extract().path("id");

        // Get
        given()
                .when()
                .get("/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("email", equalTo("restassured@example.com"));

        // Update
        UserUpdateRequest upd = new UserUpdateRequest(null, "restassured2@example.com", null, null, null);
        given()
                .contentType(ContentType.JSON)
                .body(upd)
        .when()
                .put("/" + id)
        .then()
                .statusCode(200)
                .body("email", equalTo("restassured2@example.com"));

        // Delete
        given()
                .when()
                .delete("/" + id)
                .then()
                .statusCode(204);
    }
}

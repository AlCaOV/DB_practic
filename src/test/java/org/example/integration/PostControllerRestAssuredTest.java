package org.example.integration;

import io.restassured.http.ContentType;
import org.example.dto.request.PostRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PostControllerRestAssuredTest extends TestBase {

    private static final String BASE_PATH = "/api/posts";

    @BeforeEach
    void init() {
        io.restassured.RestAssured.basePath = BASE_PATH;
    }

    @Test
    void crudFlowPost() {
        Long userId = createUser("postuser@example.com", "postuser", "pwd12345");

        PostRequest req = new PostRequest(null, userId, "Hello from RA", "Kyiv");

        Integer id = given()
                .contentType(ContentType.JSON)
                .body(req)
        .when()
                .post()
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("caption", equalTo("Hello from RA"))
                .extract().path("id");

        given()
                .when()
                .get("/" + id)
                .then()
                .statusCode(200)
                .body("caption", equalTo("Hello from RA"))
                .body("userId", equalTo(userId.intValue()));

        // cleanup
        given().when().delete("/" + id).then().statusCode(anyOf(is(204), is(200)));
    }
}


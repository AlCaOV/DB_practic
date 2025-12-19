package org.example.integration;

import org.example.dto.request.UserRequest;
import org.example.dto.request.UserUpdateRequest;
import org.example.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() { return "http://localhost:" + port + "/api/users"; }

    @Test
    void crudFlow() {
        // CREATE
        UserRequest req = new UserRequest(null, "inttest@example.com", "intuser", "password123", null);
        ResponseEntity<UserResponse> createResp = restTemplate.postForEntity(baseUrl(), req, UserResponse.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UserResponse created = createResp.getBody();
        assertThat(created).isNotNull();
        Long id = created.getId();

        // GET
        ResponseEntity<UserResponse> getResp = restTemplate.getForEntity(baseUrl() + "/" + id, UserResponse.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // UPDATE
        UserUpdateRequest updateReq = new UserUpdateRequest(null, "inttest2@example.com", null, null, null);
        HttpHeaders headers = new HttpHeaders(); headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserUpdateRequest> entity = new HttpEntity<>(updateReq, headers);
        ResponseEntity<UserResponse> updateResp = restTemplate.exchange(baseUrl() + "/" + id, HttpMethod.PUT, entity, UserResponse.class);
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // DELETE
        ResponseEntity<Void> deleteResp = restTemplate.exchange(baseUrl() + "/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}

package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.request.ProfileRequest;
import org.example.dto.response.ProfileResponse;
import org.example.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    @Test
    void postCreateShouldReturn201() throws Exception {
        ProfileRequest req = new ProfileRequest(null, "Full Name", "bio", "http://avatar");
        ProfileResponse resp = ProfileResponse.builder().userId(1L).fullName("Full Name").bio("bio").avatarUrl("http://avatar").build();

        when(profileService.create(any())).thenReturn(resp);

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void getListShouldReturn200() throws Exception {
        when(profileService.getAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/profiles")).andExpect(status().isOk());
    }
}

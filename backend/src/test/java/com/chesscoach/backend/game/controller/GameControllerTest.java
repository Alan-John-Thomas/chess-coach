package com.chesscoach.backend.game.controller;

import com.chesscoach.backend.auth.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

// Import the security request post-processor
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUserGames_Authenticated_Returns200() throws Exception {
        // 1. Create a dummy instance of OUR custom User entity
        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setEmail("magnus@chess.com");
        mockUser.setPasswordHash("hashedpassword");

        // 2. Pass it directly into the request using .with(user(mockUser))
        mockMvc.perform(get("/games").with(user(mockUser)))
                .andExpect(status().isOk());
    }

    @Test
    void getUserGames_Unauthenticated_Returns403() throws Exception {
        // No user injected here!
        mockMvc.perform(get("/games"))
                .andExpect(status().isForbidden());
    }
}
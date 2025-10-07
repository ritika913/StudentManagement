package com.example.project.loginsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testLogin_Success() throws Exception {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("testuser");
        mockUser.setPassword("testpass");

        Mockito.when(userRepository.findByUsernameAndPassword("testuser", "testpass"))
                .thenReturn(Optional.of(mockUser));

        // Act & Assert
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful!"))
                .andExpect(cookie().exists("auth"))
                .andExpect(cookie().value("auth", "true"));
    }

    @Test
    public void testLogin_Failure() throws Exception {
        // Arrange
        User loginAttempt = new User();
        loginAttempt.setUsername("wronguser");
        loginAttempt.setPassword("wrongpass");

        Mockito.when(userRepository.findByUsernameAndPassword("wronguser", "wrongpass"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginAttempt)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password!"));
    }
}

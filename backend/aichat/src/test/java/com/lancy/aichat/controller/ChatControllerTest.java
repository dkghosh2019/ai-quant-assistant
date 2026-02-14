package com.lancy.aichat.controller;

import com.lancy.aichat.service.ChatService;
import com.lancy.aichat.service.provider.AiProviderStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Unit tests for ChatController.
 *
 * <p>
 * Uses Mockito to mock ChatService. No Spring context is loaded,
 * making these true unit tests. Compatible with Spring Boot 3.4+.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChatService chatService; // Mocked service

    @InjectMocks
    private ChatController chatController; // Controller under test


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
    }

    @Test
    void testPostChatEndpointReturnsMockedResponse() throws Exception {
        // Arrange
        when(chatService.getResponse("Hello", "llama3:latest"))
                .thenReturn("Mocked AI Response");

        String requestBody = "{ \"message\": \"Hello\", \"model\": \"llama3:latest\" }";

        // Act & Assert
        mockMvc.perform(post("/api/chat")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Mocked AI Response"));

        // Verify chatService was called once
        verify(chatService, times(1)).getResponse("Hello", "llama3:latest");
    }

    @Test
    void testGetChatEndpointReturnsMockedResponse() throws Exception {
        // Arrange
        when(chatService.getResponse("Hi there", null))
                .thenReturn("Mocked GET Response");

        // Act & Assert
        mockMvc.perform(get("/api/chat")
                        .param("message", "Hi there"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Mocked GET Response"));

        verify(chatService, times(1)).getResponse("Hi there", null);
    }

    @Test
    void testPostChatEmptyMessageReturnsBadRequest() throws Exception {
        String requestBody = "{ \"message\": \"\", \"model\": \"llama3:latest\" }";

        mockMvc.perform(post("/api/chat")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        // chatService should never be called for invalid request
        verify(chatService, never()).getResponse(anyString(), anyString());
    }

    @Test
    void testPostChatWithoutModelUsesDefault() throws Exception {
        when(chatService.getResponse("Hello", null))
                .thenReturn("Mocked Default Model Response");

        String requestBody = "{ \"message\": \"Hello\" }"; // model omitted

        mockMvc.perform(post("/api/chat")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Mocked Default Model Response"));

        verify(chatService, times(1)).getResponse("Hello", null);
    }

    @Test
    void testPostChatServiceThrowsExceptionReturnsInternalServerError() throws Exception {
        when(chatService.getResponse("Hello", "llama3:latest"))
                .thenThrow(new RuntimeException("Service Failure"));

        String requestBody = "{ \"message\": \"Hello\", \"model\": \"llama3:latest\" }";

        mockMvc.perform(post("/api/chat")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isInternalServerError());

        verify(chatService, times(1)).getResponse("Hello", "llama3:latest");
    }
}

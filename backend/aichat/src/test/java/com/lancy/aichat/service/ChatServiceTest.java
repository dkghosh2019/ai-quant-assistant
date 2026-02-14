package com.lancy.aichat.service;

import com.lancy.aichat.config.AiProviderProperties;
import com.lancy.aichat.dto.ChatResponse;
import com.lancy.aichat.service.provider.AiProviderStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ChatService.
 *
 * <p>
 * Mocks AiProviderStrategy providers to test primary and fallback behavior.
 * No Spring context is loaded. Fully compatible with Java records.
 * </p>
 */
public class ChatServiceTest {

    @Mock
    private AiProviderStrategy primaryProvider;

    @Mock
    private AiProviderStrategy fallbackProvider;

    @Mock
    private AiProviderProperties properties;

    private ChatService chatService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Configure properties mock to return primary provider name
       // when(properties.getProvider()).thenReturn(() -> "PRIMARY");
        // Configure properties mock to return primary provider enum
        when(properties.getProvider()).thenReturn(AiProviderProperties.Provider.PRIMARY);



        // Create ChatService with mocked providers
        chatService = new ChatService(List.of(primaryProvider, fallbackProvider), properties);
    }

    @Test
    void testPrimaryProviderSuccess() {
        // Arrange
        when(primaryProvider.getProviderName()).thenReturn("PRIMARY");
        when(primaryProvider.chat(anyString(), any())).thenReturn(new ChatResponse("Primary Response"));
        when(fallbackProvider.getProviderName()).thenReturn("OPENAI");

        // Act
        String response = chatService.getResponse("Hello", null);

        // Assert
        assertEquals("Primary Response", response);

        // Verify interactions
        verify(primaryProvider, times(1)).chat("Hello", null);
        verify(fallbackProvider, never()).chat(anyString(), any());
    }

    @Test
    void testPrimaryFailsFallbackUsed() {
        // Arrange
        when(primaryProvider.getProviderName()).thenReturn("PRIMARY");
        when(fallbackProvider.getProviderName()).thenReturn("OPENAI");

        when(primaryProvider.chat(anyString(), any())).thenThrow(new RuntimeException("Primary Failure"));
        when(fallbackProvider.chat(anyString(), any())).thenReturn(new ChatResponse("Fallback Response"));

        // Act
        String response = chatService.getResponse("Hello", null);

        // Assert
        assertEquals("Fallback Response", response);

        // Verify interactions
        verify(primaryProvider, times(1)).chat("Hello", null);
        verify(fallbackProvider, times(1)).chat("Hello", null);
    }



    @Test
    void testNoPrimaryProviderThrowsException() {
        // Arrange: remove primary provider
        chatService = new ChatService(List.of(fallbackProvider), properties);

        // Mock getProvider() to return enum value
        when(properties.getProvider()).thenReturn(AiProviderProperties.Provider.PRIMARY);

        when(fallbackProvider.getProviderName()).thenReturn("OPENAI");

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> chatService.getResponse("Hello", null));
    }


    @Test
    void testNoFallbackProviderThrowsException() {
        // Arrange
        when(primaryProvider.getProviderName()).thenReturn("PRIMARY");
        when(primaryProvider.chat(anyString(), any())).thenThrow(new RuntimeException("Primary Failure"));

        // No fallback provider in the list
        chatService = new ChatService(List.of(primaryProvider), properties);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> chatService.getResponse("Hello", null));
    }

    @Test
    void testGetResponseWithModelOverridesConfiguredProvider() {
        // Arrange
        when(primaryProvider.getProviderName()).thenReturn("PRIMARY");
        when(primaryProvider.chat(anyString(), any())).thenReturn(new ChatResponse("Primary Response"));
        when(fallbackProvider.getProviderName()).thenReturn("OPENAI");

        // Act
        String response = chatService.getResponse("Hello", "PRIMARY");

        // Assert
        assertEquals("Primary Response", response);
        verify(primaryProvider, times(1)).chat("Hello", null);
    }
}

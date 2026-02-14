package com.lancy.aichat.dto;

import com.lancy.aichat.dto.ChatResponse;

/**
 * Strategy interface for AI provider implementations.
 *
 * <p>Any AI provider (Ollama, OpenAI, Anthropic, etc.) must implement
 * this interface. It defines the contract for executing chat requests
 * and identifying the provider.</p>
 *
 * <p>This interface enables the ChatService orchestrator to route
 * requests dynamically without coupling to a specific provider.</p>
 * 
 * @author Dipak
 */
public interface AiProviderStrategy {

    /**
     * Executes a chat request against this provider.
     *
     * @param message User input message
     * @return ChatResponse containing provider-generated output
     */
    ChatResponse chat(String message);

    /**
     * Returns the unique provider name.
     *
     * <p>Used by the orchestrator to select the provider dynamically
     * based on configuration.</p>
     *
     * @return provider name (e.g., "OLLAMA", "OPENAI", "ANTHROPIC")
     */
    String getProviderName();
}

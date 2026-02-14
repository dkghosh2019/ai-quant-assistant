package com.lancy.aichat.service.provider;

import com.lancy.aichat.dto.ChatResponse;

/**
 * Strategy interface for AI provider implementations.
 *
 * <p>Any AI provider (Ollama, OpenAI, Anthropic, etc.) must implement
 * this interface. It defines the contract for executing chat requests
 * and identifying the provider.</p>
 *
 * <p>This interface enables the ChatService orchestrator to route
 * requests dynamically and inject custom system prompts.</p>
 */
public interface AiProviderStrategy {

    /**
     * Executes a chat request against this provider.
     *
     * @param message    User input message
     * @param systemPrompt Optional system prompt to override default persona/context
     * @return ChatResponse from the provider
     */
    ChatResponse chat(String message, String systemPrompt);

    /**
     * Returns the unique provider name.
     *
     * Used by the orchestrator for routing.
     *
     * @return provider name (e.g., "OLLAMA", "OPENAI", "ANTHROPIC")
     */
    String getProviderName();
}

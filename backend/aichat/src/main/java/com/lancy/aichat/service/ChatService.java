package com.lancy.aichat.service;

import com.lancy.aichat.config.AiProviderProperties;
import com.lancy.aichat.dto.ChatRequest;
import com.lancy.aichat.dto.ChatResponse;
import com.lancy.aichat.service.provider.AiProviderStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Central orchestrator for AI chat interactions.
 *
 * <p>
 * Routes requests to the appropriate AI provider implementation based on active configuration
 * in {@link AiProviderProperties}. Provides a simple getResponse method for controller usage.
 * </p>
 *
 * <p>
 * Features:
 * <ul>
 *     <li>Dynamic system prompt injection per request</li>
 *     <li>Fallback to OpenAI if primary provider fails</li>
 *     <li>Supports unit testing via simple getResponse method</li>
 *     <li>Structured logging for observability</li>
 * </ul>
 * </p>
 */
@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    /** List of AI provider strategies available in the system */
    private final List<AiProviderStrategy> providers;

    /** Configuration for selecting active AI provider */
    private final AiProviderProperties properties;

    public ChatService(List<AiProviderStrategy> providers,
                       AiProviderProperties properties) {
        this.providers = providers;
        this.properties = properties;
    }

    /**
     * Controller-friendly method to get a response from the AI.
     * Can be mocked in unit tests.
     *
     * @param message user message
     * @param model optional model name (overrides configured provider if provided)
     * @return AI-generated response text
     */
    public String getResponse(String message, String model) {
        // Construct ChatRequest with null for optional fields (systemPrompt, sessionId)
        ChatRequest request = new ChatRequest(message, null, null, null);

        // If a model is provided, override the configured provider
        if (model != null && !model.isBlank()) {
            return routeToProvider(message, model, request.systemPrompt()).response();
        }

        // Use the provider from properties configuration
        String configuredProvider = properties.getProvider().name();
        return routeToProvider(message, configuredProvider, request.systemPrompt()).response();
    }

    /**
     * Core routing logic: routes message to the given provider name,
     * applies fallback if necessary.
     *
     * @param message user message
     * @param providerName primary provider name
     * @param systemPrompt optional system prompt
     * @return ChatResponse from selected or fallback provider
     */
    private ChatResponse routeToProvider(String message, String providerName, String systemPrompt) {

        // Find primary provider
        AiProviderStrategy primaryProvider = providers.stream()
                .filter(p -> p.getProviderName().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No AI provider found for: " + providerName));

        try {
            log.info("Routing message '{}' to primary provider: {}", message, primaryProvider.getProviderName());
            return primaryProvider.chat(message, systemPrompt);

        } catch (Exception e) {
            log.warn("Primary provider {} failed. Error: {}. Attempting fallback to OPENAI.",
                    primaryProvider.getProviderName(), e.getMessage());

            // Fallback provider (OpenAI)
            AiProviderStrategy fallbackProvider = providers.stream()
                    .filter(p -> p.getProviderName().equalsIgnoreCase("OPENAI"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No fallback provider available"));

            return fallbackProvider.chat(message, systemPrompt);
        }
    }

    /**
     * Existing DTO-based method that uses ChatRequest.
     *
     * @param request ChatRequest
     * @return ChatResponse
     */
    public ChatResponse chat(ChatRequest request) {
        return routeToProvider(request.message(), properties.getProvider().name(), request.systemPrompt());
    }
}

package com.lancy.aichat.dto;

/**
 * DTO representing a chat request from the user.
 *
 * <p>Contains the user message, session ID, and optional
 * system prompt for dynamic persona/context injection.</p>
 */
public record ChatRequest(
        String message,
        String sessionId,
        String systemPrompt  , // Optional: overrides default system prompt
        String model
) {}

package com.lancy.aichat.model;

/**
 * Enum representing supported AI providers.
 *
 * <p>This enum is used by {@link com.lancy.aichat.config.AiProviderProperties}
 * to determine which AI provider the ChatService orchestrator should route to.</p>
 *
 * Supported providers:
 * <ul>
 *     <li>OLLAMA – Local LLM runtime</li>
 *     <li>OPENAI – OpenAI API</li>
 *     <li>ANTHROPIC – Anthropic API</li>
 * </ul>
 * 
 * Author: Dipak
 */
public enum AiProvider {
    OLLAMA,
    OPENAI,
    ANTHROPIC
}

package com.lancy.aichat.service.provider;

import com.lancy.aichat.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * Ollama implementation of {@link AiProviderStrategy}.
 *
 * <p>This class communicates with a locally running Ollama LLM
 * using Spring AI's {@link ChatClient}.</p>
 *
 * <p>System prompt defines the AI Quantitative Trading Assistant persona.</p>
 */
@Component
public class OllamaProvider implements AiProviderStrategy {

    private final ChatClient chatClient;

    /**
     * Constructor injection for Spring AI ChatClient.
     */
    public OllamaProvider(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Sends a user message to Ollama and returns a structured {@link ChatResponse}.
     */

    @Override
    public ChatResponse chat(String message, String systemPrompt) {

        String prompt = systemPrompt != null ? systemPrompt
                : "You are an AI Quantitative Trading Assistant. Be precise, analytical, focus on risk management.";

        String response = chatClient.prompt()
                .system(prompt)
                .user(message)
                .call()
                .content();

        return new ChatResponse(response);
    }


    /**
     * Provider name for routing.
     */
    @Override
    public String getProviderName() {
        return "OLLAMA";
    }
}

package com.lancy.aichat.service.provider;

import com.lancy.aichat.dto.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * Anthropic implementation of {@link AiProviderStrategy}.
 *
 * <p>This class communicates with the Anthropic API using Spring AI's {@link ChatClient}.</p>
 *
 * <p>Supports dynamic system prompt injection and logs each request and response
 * for monitoring and metrics purposes.</p>
 *
 * <p>Configuration (API key, model) is handled via Spring AI properties.</p>
 */
@Component
public class AnthropicProvider implements AiProviderStrategy {

    private static final Logger log = LoggerFactory.getLogger(AnthropicProvider.class);

    private final ChatClient chatClient;

    /**
     * Constructor injection for Spring AI ChatClient.
     *
     * @param builder ChatClient builder auto-configured by Spring Boot
     */
    public AnthropicProvider(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Sends a user message to Anthropic and returns a structured {@link ChatResponse}.
     *
     * <p>If {@code systemPrompt} is null, the default Quantitative Trading Assistant
     * prompt is used.</p>
     *
     * @param message      user input message
     * @param systemPrompt optional system prompt to override default persona
     * @return ChatResponse containing Anthropic-generated reply
     */
    @Override
    public ChatResponse chat(String message, String systemPrompt) {

        String prompt = (systemPrompt != null && !systemPrompt.isBlank())
                ? systemPrompt
                : "You are an AI Quantitative Trading Assistant. " +
                "Be precise. Be analytical. Focus on risk management.";

        log.info("Sending message to Anthropic: {}, using system prompt: {}", message, prompt);

        String response = chatClient.prompt()
                .system(prompt)
                .user(message)
                .call()
                .content();

        log.info("Received response from Anthropic: {}", response);

        return new ChatResponse(response);
    }

    /**
     * Returns the provider name for routing in the orchestrator.
     *
     * @return "ANTHROPIC"
     */
    @Override
    public String getProviderName() {
        return "ANTHROPIC";
    }
}

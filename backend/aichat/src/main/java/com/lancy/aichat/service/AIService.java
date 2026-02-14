package com.lancy.aichat.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * AIService is responsible for interacting with the underlying Large Language Model (LLM)
 * via Spring AI's {@link ChatClient}.
 *
 * <p>This service encapsulates all AI-related communication logic and acts as a bridge
 * between the REST controller layer and the LLM provider (e.g., Ollama).
 *
 * <p>Responsibilities:
 * <ul>
 *     <li>Building prompts</li>
 *     <li>Injecting system instructions</li>
 *     <li>Sending user input to the model</li>
 *     <li>Returning model-generated responses</li>
 * </ul>
 *
 * <p>This design ensures separation of concerns and keeps AI orchestration logic
 * isolated from the web/controller layer.
 */
@Service
public class AIService {

    /**
     * Spring AI ChatClient used to communicate with the configured LLM.
     */
    private final ChatClient chatClient;

    /**
     * Constructs an AIService using a {@link ChatClient.Builder}.
     *
     * <p>The builder is auto-configured by Spring Boot based on the
     * application properties (e.g., Ollama model configuration).
     *
     * @param builder injected ChatClient builder
     */
    public AIService(ChatClient.Builder builder) {
        // Build a ChatClient instance from the provided builder
        this.chatClient = builder.build();
    }

    /**
     * Sends a user message to the LLM and returns the generated response.
     *
     * <p>This method:
     * <ol>
     *     <li>Creates a prompt</li>
     *     <li>Sets a system instruction to guide model behavior</li>
     *     <li>Adds the user's message</li>
     *     <li>Executes the LLM call</li>
     *     <li>Extracts the textual content from the response</li>
     * </ol>
     *
     * @param message the user input message
     * @return the AI-generated response as plain text
     */
    public String getResponse(String message) {

        return chatClient.prompt()

                // System message defines model behavior and constraints
                .system("You are an AI Quantitative Trading Assistant. " +
                        "Be precise. Be analytical. Focus on risk management.")

                // User message provided dynamically from request
                .user(message)

                // Execute the request to the LLM
                .call()

                // Extract only the generated text content
                .content();
    }
}

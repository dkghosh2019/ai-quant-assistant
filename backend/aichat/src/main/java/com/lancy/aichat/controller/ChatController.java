package com.lancy.aichat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for AI chat interactions.
 *
 * <p>
 * This controller exposes both GET and POST endpoints (`/api/chat`)
 * that allow clients to send messages to the AI model (via Spring AI ChatClient)
 * and receive AI-generated responses.
 * </p>
 *
 * <p>
 * Key features:
 * <ul>
 *     <li>Uses ChatClient from Spring AI for interacting with Ollama or other LLMs</li>
 *     <li>Supports both GET (quick testing) and POST (frontend integration)</li>
 *     <li>JSON request and response for clean API design</li>
 *     <li>Structured logging for observability</li>
 *     <li>Ready for enhancements like memory, streaming, or tool-calling</li>
 * </ul>
 * </p>
 *
 * <p>
 * CORS is configured globally in {@link com.lancy.aichat.config.WebConfig}.
 * </p>
 */
@RestController
@RequestMapping("/api")
@Validated
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    /**
     * Centralized system prompt that defines AI behavior.
     * Keeping this constant ensures consistent responses across endpoints.
     */
    private static final String SYSTEM_PROMPT =
            "You are an AI Quantitative Trading Assistant. " +
                    "Be precise. Be analytical. Focus on risk management.";

    /**
     * Spring AI ChatClient for sending prompts to the AI model.
     */
    private final ChatClient chatClient;

    /**
     * Constructor injection of ChatClient builder.
     *
     * @param builder ChatClient.Builder provided by Spring AI auto-configuration
     */
    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * GET endpoint for quick testing via browser.
     *
     * <p>
     * Example:
     * <pre>
     * http://localhost:8080/api/chat?message=Explain risk reward ratio
     * </pre>
     *
     * @param message user message passed as query parameter
     * @return ChatResponse containing AI-generated reply
     */
    @GetMapping("/chat")
    public ChatResponse chatGet(
            @RequestParam(defaultValue = "Tell me a trading joke") String message) {

        log.info("Received GET request with message: {}", message);

        String response = generateAIResponse(message);

        return new ChatResponse(response);
    }

    /**
     * POST endpoint for structured frontend integration (e.g., Angular).
     *
     * <p>
     * Example request body:
     * <pre>
     * {
     *     "message": "Explain black holes in simple terms"
     * }
     * </pre>
     *
     * @param request ChatRequest containing the user message
     * @return ChatResponse containing AI-generated reply
     */
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {

        log.info("Received POST request with message: {}", request.message());

        try {
            String response = generateAIResponse(request.message());
            return new ChatResponse(response);

        } catch (Exception e) {

            log.error("Error while calling AI model", e);

            return new ChatResponse("Error: Unable to process request at this time.");
        }
    }

    /**
     * Internal helper method to generate AI responses.
     *
     * <p>
     * This method centralizes prompt construction logic so that both
     * GET and POST endpoints reuse the same AI orchestration flow.
     * </p>
     *
     * @param userMessage message provided by the client
     * @return generated AI response text
     */
    private String generateAIResponse(String userMessage) {

        return chatClient.prompt()

                // Inject system instruction to guide model behavior
                .system(SYSTEM_PROMPT)

                // Add user message
                .user(userMessage)

                // Execute synchronous LLM call
                .call()

                // Extract response text
                .content();
    }

    /**
     * Request DTO for the chat endpoint.
     *
     * <p>
     * Example JSON:
     * <pre>
     * { "message": "Hello AI!" }
     * </pre>
     *
     * @param message user input message
     */
    public record ChatRequest(String message) {}

    /**
     * Response DTO for the chat endpoint.
     *
     * <p>
     * Example JSON:
     * <pre>
     * { "response": "Hello! How can I help you today?" }
     * </pre>
     *
     * @param response AI-generated output
     */
    public record ChatResponse(String response) {}
}

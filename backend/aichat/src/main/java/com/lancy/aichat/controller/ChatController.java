package com.lancy.aichat.controller;

import com.lancy.aichat.service.ChatService;
import com.lancy.aichat.dto.ChatRequest;
import com.lancy.aichat.dto.ChatResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for handling chat-related HTTP requests.
 *
 * <p>
 * This controller exposes endpoints for interacting with the AI chat service.
 * It supports both GET and POST methods for flexibility:
 * </p>
 *
 * <ul>
 *     <li>GET  /api/chat  - Simple query-based chat request</li>
 *     <li>POST /api/chat  - JSON body-based chat request</li>
 * </ul>
 *
 * <p>
 * The controller performs:
 * </p>
 * <ul>
 *     <li>Input validation</li>
 *     <li>Logging for observability</li>
 *     <li>Error handling with proper HTTP status codes</li>
 * </ul>
 *
 * <p>
 * Business logic is delegated to {@link ChatService}.
 * </p>
 */
@RestController
@RequestMapping("/api")
public class ChatController {

    /**
     * Logger instance for structured logging.
     */
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    /**
     * Service responsible for communicating with the AI provider.
     */
    private final ChatService chatService;

    /**
     * Constructor-based dependency injection.
     *
     * @param chatService the service that handles AI response generation
     */
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Handles GET requests for chat interactions.
     *
     * <p>
     * Example:
     * <pre>
     * GET /api/chat?message=Hello&model=llama3
     * </pre>
     * </p>
     *
     * @param message the user message (required)
     * @param model   optional AI model name
     * @return {@link ResponseEntity} containing {@link ChatResponse}
     */
    @GetMapping("/chat")
    public ResponseEntity<ChatResponse> chatGet(
            @RequestParam String message,
            @RequestParam(required = false) String model) {

        log.info("Received GET request with message: {}", message);

        // Validate message input
        if (message == null || message.trim().isEmpty()) {
            log.warn("Invalid GET request: message is empty");
            return ResponseEntity
                    .badRequest()
                    .body(new ChatResponse("Message cannot be empty."));
        }

        try {
            // Delegate response generation to service layer
            String response = chatService.getResponse(message, model);

            return ResponseEntity.ok(new ChatResponse(response));

        } catch (Exception e) {
            // Log error for monitoring and debugging
            log.error("Error while calling AI model (GET)", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChatResponse("Error: Unable to process request at this time."));
        }
    }

    /**
     * Handles POST requests for chat interactions.
     *
     * <p>
     * Example:
     * <pre>
     * POST /api/chat
     * {
     *   "message": "Hello",
     *   "model": "llama3",
     *   "sessionId": "123",
     *   "systemPrompt": "You are a helpful assistant"
     * }
     * </pre>
     * </p>
     *
     * @param request {@link ChatRequest} containing user message and optional metadata
     * @return {@link ResponseEntity} containing {@link ChatResponse}
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {

        log.info("Received POST request with message: {}", request.message());

        // Validate input
        if (request.message() == null || request.message().trim().isEmpty()) {
            log.warn("Invalid POST request: message is empty");
            return ResponseEntity
                    .badRequest()
                    .body(new ChatResponse("Message cannot be empty."));
        }

        try {
            // Delegate to service layer (business logic)
            String response = chatService.getResponse(
                    request.message(),
                    request.model()
            );

            return ResponseEntity.ok(new ChatResponse(response));

        } catch (Exception e) {
            // Log exception for observability
            log.error("Error while calling AI model (POST)", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChatResponse("Error: Unable to process request at this time."));
        }
    }
}

package com.lancy.aichat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for the application.
 * <p>
 * This configuration allows the Angular frontend (running on http://localhost:4200)
 * to make HTTP requests to the Spring Boot backend without running into
 * cross-origin issues.
 * </p>
 * <p>
 * Key settings:
 * <ul>
 *     <li>allowedOrigins: Frontend URL (Angular)</li>
 *     <li>allowedMethods: HTTP methods including OPTIONS for preflight</li>
 *     <li>allowedHeaders: Allows Content-Type, Authorization, and other headers</li>
 *     <li>allowCredentials: Supports cookies and authentication headers</li>
 * </ul>
 * </p>
 * <p>
 * Notes for production:
 * <ul>
 *     <li>If Spring Security is added later, CORS must also be configured in the SecurityFilterChain.</li>
 *     <li>For multiple frontend domains, consider using a property or list of allowed origins.</li>
 * </ul>
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure global CORS mappings for the application.
     *
     * @param registry The CorsRegistry to add mappings to
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS to all endpoints
                .allowedOrigins("http://localhost:4200") // Allow Angular frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS needed for preflight requests
                .allowedHeaders("*") // Angular sends Content-Type: application/json
                // Future auth tokens require headers
                .allowCredentials(true); // Allow cookies or auth headers
        // OPTIONS preflight requests need support
    }
}

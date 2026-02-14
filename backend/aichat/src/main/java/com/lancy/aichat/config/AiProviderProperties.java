package com.lancy.aichat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Configuration for AI provider selection.
 */

@Component
@ConfigurationProperties(prefix = "ai.provider")
public class AiProviderProperties {

    private Provider provider = Provider.PRIMARY; // default

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public enum Provider {
        PRIMARY,
        OPENAI
    }
}

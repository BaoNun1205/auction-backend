package com.example.auction_web.personalization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;

@Configuration
public class EmbeddingAIConfig {

    @Value("${openai.embedding.endpoint}")
    private String endpoint;

    @Value("${openai.embedding.api-key}")
    private String apiKey;

    @Bean(name = "embeddingOpenAIClient")
    public OpenAIClient openAIClient() {
        return new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey))
                .buildClient();
    }    
}


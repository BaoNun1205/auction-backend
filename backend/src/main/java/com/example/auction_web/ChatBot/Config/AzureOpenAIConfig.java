package com.example.auction_web.ChatBot.Config;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureOpenAIConfig {
    @Value("${openai.azure.api-key}")
    private String azureApiKey;

    @Value("${openai.azure.endpoint}")
    private String azureEndpoint;

    @Bean(name = "azureOpenAIClient")
    public OpenAIClient openAIClient() {
        AzureKeyCredential credential = new AzureKeyCredential(azureApiKey);
        return new OpenAIClientBuilder()
                .endpoint(azureEndpoint)
                .credential(credential)
                .buildClient();
    }
}

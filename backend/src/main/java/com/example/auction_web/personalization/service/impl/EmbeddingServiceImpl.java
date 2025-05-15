package com.example.auction_web.personalization.service.impl;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.example.auction_web.personalization.service.EmbeddingService;

@Service
public class EmbeddingServiceImpl implements EmbeddingService {

    
    OpenAIClient openAIClient;

    @Value("${openai.embedding.deployment-name}")
    String deploymentName;

    public EmbeddingServiceImpl(@Qualifier("embeddingOpenAIClient") OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    @Override
    public List<Float> getEmbeddingFromText(String inputText) {
        EmbeddingsOptions options = new EmbeddingsOptions(List.of(inputText));

        Embeddings embeddings = openAIClient.getEmbeddings(deploymentName, options);

        List<EmbeddingItem> embeddingItems = embeddings.getData();
        if (embeddingItems != null && !embeddingItems.isEmpty()) {
            return embeddingItems.get(0).getEmbedding();
        }

        return List.of();
    }
}



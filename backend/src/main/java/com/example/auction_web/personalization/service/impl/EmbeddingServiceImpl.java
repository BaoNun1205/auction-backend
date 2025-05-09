package com.example.auction_web.personalization.service.impl;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.example.auction_web.personalization.service.EmbeddingService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmbeddingServiceImpl implements EmbeddingService {
    OpenAIClient openAIClient;

    @Override
    public List<Float> getEmbeddingFromText(String inputText) {
        EmbeddingsOptions options = new EmbeddingsOptions(List.of(inputText));

        Embeddings embeddings = openAIClient.getEmbeddings("text-embedding-ada-002", options);

        List<EmbeddingItem> embeddingItems = embeddings.getData();
        if (embeddingItems != null && !embeddingItems.isEmpty()) {
            return embeddingItems.get(0).getEmbedding();
        }

        return List.of();
    }
}


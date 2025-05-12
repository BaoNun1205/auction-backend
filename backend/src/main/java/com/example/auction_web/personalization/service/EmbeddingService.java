package com.example.auction_web.personalization.service;

import java.util.List;

public interface EmbeddingService {
    List<Float> getEmbeddingFromText(String inputText);
}

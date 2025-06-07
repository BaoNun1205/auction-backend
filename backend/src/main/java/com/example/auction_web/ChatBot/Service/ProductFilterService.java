package com.example.auction_web.ChatBot.Service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.*;
import com.example.auction_web.dto.response.TypeFilterResponse;
import com.example.auction_web.service.TypeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductFilterService {
    @Value("${openai.azure.deployment-name}")
    private String deploymentName;

    private final OpenAIClient openAIClient;
    
    @Autowired
    private TypeService typeService;

    public ProductFilterService(@Qualifier("azureOpenAIClient") OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public Map<String, String> classifyProduct(String name, String description, List<String> imageUrls) {
        List<TypeFilterResponse> descriptionCategories = typeService.getAllTypeFilterResponse(); 
        String prompt = buildPrompt(name, description, imageUrls, descriptionCategories);
    
        List<ChatRequestMessage> messages = new ArrayList<>();
        messages.add(new ChatRequestSystemMessage("""
            You are an expert assistant for an online auction platform with years of experience in evaluating and classifying rare, valuable, and historical items.
            Your primary responsibility is to classify products into the most appropriate auction category based on their name, description, and images (provided via URLs).
            You always respond clearly, accurately, and in the same language as the user input. If you're unsure about a classification, respond with {"category": "Uncertain"}.
        """));
        messages.add(new ChatRequestUserMessage(prompt));
    
        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        options.setTemperature(1.5);
        ChatCompletions result = openAIClient.getChatCompletions(deploymentName, options);
    
        String content = result.getChoices().get(0).getMessage().getContent();
        return extractTypeInfo(content);
    }
    
    public Map<String, String> extractTypeInfo(String content) {
        try {
            JsonNode node = new ObjectMapper().readTree(content);
            System.out.println(node.toString());
            Map<String, String> rs = new HashMap<>();
            rs.put("typeId", node.get("typeId").asText());
            
            if (node.has("type")) {
                rs.put("type", node.get("type").asText());
            }
            // Nếu phản hồi có key "category" (tùy bạn đặt prompt)
            else if (node.has("category")) {
                rs.put("type", node.get("category").asText());
            } else {
                rs.put("type", "Unknown");
            }

            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> fallback = new HashMap<>();
            fallback.put("typeId", "uncertain");
            fallback.put("type", "Uncertain");
            return fallback;
        }
    }

    private String buildPrompt(String name, String description, List<String> imageUrls, List<TypeFilterResponse> descriptionCategories) {
        String images = "";
        if (imageUrls != null && !imageUrls.isEmpty()) {
            images = "\nImage Information:\n";
            for (int i = 0; i < imageUrls.size(); i++) {
                images += "- Image " + (i + 1) + ": " + imageUrls.get(i) + "\n";
            }
            images += "\n(Note: Analyze image content based on the context from the name and description, since images are provided as URLs and cannot be visually rendered.)";
        }

        // Xây dựng danh sách category với typeId và type
        StringBuilder categoryList = new StringBuilder();
        for (TypeFilterResponse category : descriptionCategories) {
            categoryList.append("- ")
                        .append(category.getTypeName())
                        .append(" (typeId: ")
                        .append(category.getTypeId())
                        .append(")\n");
        }


        String test = """
            You are a professional product classification assistant for an online auction platform with deep expertise in rare, valuable, and niche items.

            Your task is to analyze a product's name, description, and image URLs to determine the most appropriate category.

            📌 Response must follow these strict rules:
            - Always respond in the same language as the user input.
            - The response must be a single, valid JSON object in the following format:
            {"typeId": "<TYPE_ID>", "type": "<TYPE_NAME>"}

            Recognized categories (type with typeId):
            %s

            🧠 Advanced instructions:
            - If the product does not clearly belong to any of the above categories, suggest a new category and set typeId to "new".
            - If you are uncertain after analyzing all information, respond with:
            {"typeId": "uncertain", "type": "Uncertain"}

            🚫 Do NOT return multiple categories. Return only one best-fit category.

            Product details:
            Name: %s
            Description: %s
            %s
            """.formatted(categoryList.toString(), name, description, images);

        return test;
    }
}

package com.example.auction_web.ChatBot.Service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductFilterService {
    @Value("${openai.azure.deployment-name}")
    private String deploymentName;

    private final OpenAIClient openAIClient;

    public ProductFilterService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public String classifyProduct(String name, String description, List<String> imageUrls) {
        String prompt = buildPrompt(name, description, imageUrls);

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

        try {
            JsonNode node = new ObjectMapper().readTree(content);
            return node.get("category").asText();
        } catch (Exception e) {
            return "Uncertain";
        }
    }

    private String buildPrompt(String name, String description, List<String> imageUrls) {
        String images = "";
        if (imageUrls != null && !imageUrls.isEmpty()) {
            images = "\nImage Information:\n";
            for (int i = 0; i < imageUrls.size(); i++) {
                images += "- Image " + (i + 1) + ": " + imageUrls.get(i) + "\n";
            }
            images += "\n(Note: Analyze image content based on the context from the name and description, since images are provided as URLs and cannot be visually rendered.)";
        }

        return """
        You are a professional product classification assistant for an online auction platform with deep expertise in rare, valuable, and niche items.

        Your task is to analyze a product's name, description, and image URLs to determine the most appropriate category.
        
        📌 Response must follow these strict rules:
        - Always respond in the same language as the user input.
        - The response must be a single, valid JSON object in the following format:
          {"category": "<CATEGORY_NAME>"}

        Commonly recognized categories:
            - "Ceramic Vases", "Antique Plates", "Antique Bowls", "Porcelain Tea Sets", "Antique Cups",\s
              "Chinese Porcelain", "Japanese Pottery", "Korean Ceramics", "Delftware", "Art Deco Tableware",\s
              "Antique Mugs", "Hand-Painted Dishes", "Antique Pitchers", "Ming Dynasty Ceramics", "Antique Serving Platters",\s
              "Antique Cabinets", "Victorian Furniture", "Baroque Furniture", "Louis XIV Furniture", "Art Nouveau Chairs",\s
              "Antique Chests", "Colonial Furniture", "Renaissance Furniture", "Art Deco Furniture", "Biedermeier Furniture",\s
              "Rococo Tables", "Edwardian Furniture", "Antique Desks", "Georgian Furniture", "Antique Mirrors",\s
              "Carved Wooden Figures", "Bronze Statues", "Antique Candlesticks", "Copper Coins", "Brass Lamps",\s
              "Silverware", "Old Bronze Coins", "Bronze Mirrors", "Cast Iron Stoves", "Victorian Brass Door Knockers",\s
              "Bronze Plaques", "Antique Silver Candelabras", "Silver Tea Sets", "Copper Pots", "Bronze Medals",\s
              "Vintage Rings", "Antique Necklaces", "Art Deco Brooches", "Victorian Bracelets", "Cameo Brooches",\s
              "Estate Jewelry", "Antique Watches", "Vintage Earrings", "Vintage Lockets", "Georgian Jewelry", "Antique Charms",\s
              "Tiffany Jewelry", "Bangle Bracelets", "Platinum Engagement Rings", "Pearl Necklaces",\s
              "Antique Paintings", "Renaissance Art", "Baroque Sculptures", "Antique Portraits", "Asian Calligraphy",\s
              "Impressionist Paintings", "Vintage Lithographs", "Art Nouveau Glass", "Oil Paintings", "Antique Stained Glass",\s
              "Antique Maps", "Antique Mirrors", "Bronze Sculptures", "Old Master Paintings", "Watercolor Paintings",\s
              "Asian Screen Paintings", "Antique Violins", "Old Pianos", "Vintage Guitars", "Wind Instruments",\s
              "String Instruments", "Music Boxes", "Antique Horns", "Vintage Saxophones", "Antique Flutes",\s
              "Musical Clocks", "Vintage Record Players", "Antique Ocarinas", "Victorian Organs", "Trumpets", "Old Drums",\s
              "Rare Books", "First Editions", "Ancient Manuscripts", "Illuminated Manuscripts", "Antique Maps",\s
              "Signed Books", "Vintage Magazines", "Old Newspapers", "16th Century Books", "Victorian Literature",\s
              "Antique Encyclopedias", "Old Diaries", "Historical Documents", "Ancient Scrolls", "Ancient Texts",\s
              "Antique Clocks", "Grandfather Clocks", "Vintage Pocket Watches", "Art Deco Clocks", "Mantel Clocks",\s
              "Carriage Clocks", "Vintage Wall Clocks", "Mechanical Watches", "Swiss Watches", "Clock Pendulums",\s
              "Nautical Clocks", "Fob Watches", "Vintage Alarm Clocks", "Railroad Clocks", "Vintage Timepieces",\s
              "Classic Cars", "Vintage Motorcycles", "Antique Bicycles", "Old Boats", "Rare Automobiles", "Antique Carriages",\s
              "Vintage Tractors", "Vintage Motor Scooters", "Steam Engines", "Vintage Fire Trucks", "Military Vehicles",\s
              "Antique Horse Drawn Carts", "Vintage Go-Karts", "Antique Motorbikes", "Antique Pedal Cars",\s
              "Antique Teapots", "Old Cooking Pots", "Vintage Kitchen Scales", "Cast Iron Skillets", "Antique Coffee Grinders",\s
              "Vintage Coffee Pots", "Antique Ice Boxes", "Wooden Cutting Boards", "Old Gravy Boats", "Retro Toasters",\s
              "Antique Spice Containers", "Vintage Salt and Pepper Shakers", "Enamelware", "Vintage Canisters",\s
              "Old Water Jugs", "Ancient Pottery", "Egyptian Artifacts", "Roman Relics", "Greek Sculptures",\s
              "Medieval Armor", "Viking Artifacts", "Ancient Jewelry", "Native American Artifacts", "Pre-Columbian Artifacts",\s
              "Ancient Weapons", "Old Religious Artifacts", "Pharaoh’s Relics", "Ancient Tools", "Antique Stone Carvings",\s
              "Old Bronze Weapons", "Handwoven Rugs", "Embroidered Textiles", "Vintage Quilts", "Antique Lace",\s
              "Handcrafted Wooden Boxes", "Old Needlework", "Hand-carved Figurines", "Vintage Pottery", "Hand-painted Ceramics",\s
              "Antique Embroidery", "Handcrafted Glassware", "Vintage Sewing Machines", "Hand-forged Metalwork",\s
              "Handmade Jewelry", "Antique Woven Baskets", "African Masks", "Asian Ceremonial Artifacts", "Native American Pottery",\s
              "Indian Tapestries", "Persian Rugs", "South American Textiles", "African Sculptures", "Aboriginal Artifacts",\s
              "Polynesian Carvings", "Tibetan Art", "Ancient Chinese Ceramics", "Japanese Samurai Swords",\s
              "Middle Eastern Metalwork", "Mongolian Armor", "Mayan Artifacts"

        🧠 Advanced instructions:
        - If the product does not clearly belong to any of the above categories, suggest a new category that best fits the product.
        - If you are uncertain after analyzing all information, respond with:
          {"category": "Uncertain"}

        🚫 Do NOT return multiple categories. Return only one best-fit category.

        Product details:
        Name: %s
        Description: %s
        %s
        """.formatted(name, description, images);
    }

}

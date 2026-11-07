package com.custom.oauthagent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AiInvestigatorClient {

    private static final String API_URL = "https://openai.com";
    private final String apiKey;
    private final Gson gson = new Gson();

    public AiInvestigatorClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String analyzeProvider(String providerName, String scrapedContent) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String systemInstruction = "You are a professional security and integration auditor. " +
                    "Analyze the provided documentation text for the requested OAuth provider. " +
                    "Output a clean, structural text guide containing exactly: " +
                    "1. Setup Difficulty (Easy, Medium, or Hard), " +
                    "2. Estimated Timeline, " +
                    "3. Step-by-Step UI Navigation Steps to find Client ID/Secret, " +
                    "4. Known Blockers or Review Requirements.";

            String userPrompt = "Analyze this provider: " + providerName + "\n\nDocumentation context:\n" + scrapedContent;

            JsonObject mainBody = new JsonObject();
            mainBody.addProperty("model", "gpt-4o-mini");

            JsonArray messagesArray = new JsonArray();

            JsonObject sysMsg = new JsonObject();
            sysMsg.addProperty("role", "system");
            sysMsg.addProperty("content", systemInstruction);
            messagesArray.add(sysMsg);

            JsonObject usrMsg = new JsonObject();
            usrMsg.addProperty("role", "user");
            usrMsg.addProperty("content", userPrompt);
            messagesArray.add(usrMsg);

            mainBody.add("messages", messagesArray);
            String jsonPayload = gson.toJson(mainBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                return jsonResponse.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();
            } else {
                return "Error calling AI engine. Status code: " + response.statusCode() + " - " + response.body();
            }

        } catch (Exception e) {
            return "Failed during AI investigation layer analysis: " + e.getMessage();
        }
    }
}

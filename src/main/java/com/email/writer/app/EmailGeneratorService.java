package com.email.writer.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;
    private final EmailContextService contextService;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorService(WebClient.Builder webClientBuilder, EmailContextService contextService) {
        this.webClient = webClientBuilder.build();
        this.contextService = contextService;
    }

    public String generateEmailReply(EmailRequest emailRequest) {
        String prompt = buildPrompt(emailRequest);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                       Map.of("parts", new Object[]{
                               Map.of("text", prompt)
                       })
                },
                "generationConfig", Map.of(
                    "temperature", 0.7,
                    "maxOutputTokens", 200,
                    "topP", 0.8
                )
        );

        try {
            String response = webClient.post()
                    .uri(geminiApiUrl + geminiApiKey)
                    .header("Content-Type","application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return extractResponseContent(response);
        } catch (Exception e) {
            return "I apologize, but I'm unable to generate a reply at the moment. Please try again.";
        }
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            String content = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
            
            return content.trim();
        } catch (Exception e) {
            return "Error processing response. Please try again.";
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        String contextualGuidance = contextService.getContextualPrompt(
            emailRequest.getEmailContent(), 
            emailRequest.getTone()
        );
        
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a professional email assistant. Generate a concise, contextually appropriate email reply.\n\n");
        prompt.append("Guidelines:\n");
        prompt.append("- Match the formality level of the original email\n");
        prompt.append("- Be direct and actionable\n");
        prompt.append("- Keep it under 150 words\n");
        prompt.append("- Don't include subject line, signatures, or greetings\n");
        prompt.append("- Address the main points raised\n");
        prompt.append("- ").append(contextualGuidance).append("\n\n");
        
        prompt.append("Original email content:\n").append(emailRequest.getEmailContent());
        prompt.append("\n\nGenerate only the reply content:");
        return prompt.toString();
    }
}

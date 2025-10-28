package com.email.writer.app;

import org.springframework.stereotype.Service;

@Service
public class EmailContextService {
    
    public String detectTone(String emailContent) {
        String content = emailContent.toLowerCase();
        
        if (content.contains("urgent") || content.contains("asap") || content.contains("immediately")) {
            return "urgent";
        }
        if (content.contains("thank") || content.contains("appreciate") || content.contains("grateful")) {
            return "grateful";
        }
        if (content.contains("sorry") || content.contains("apologize") || content.contains("mistake")) {
            return "apologetic";
        }
        if (content.contains("meeting") || content.contains("schedule") || content.contains("appointment")) {
            return "scheduling";
        }
        
        return "professional";
    }
    
    public String getContextualPrompt(String emailContent, String requestedTone) {
        String detectedTone = detectTone(emailContent);
        String finalTone = requestedTone != null && !requestedTone.isEmpty() ? requestedTone : detectedTone;
        
        return switch (finalTone) {
            case "urgent" -> "Respond with urgency and provide clear next steps.";
            case "grateful" -> "Acknowledge their thanks and maintain positive tone.";
            case "apologetic" -> "Accept gracefully and focus on solutions.";
            case "scheduling" -> "Be specific about availability and confirm details.";
            default -> "Maintain professional courtesy.";
        };
    }
}

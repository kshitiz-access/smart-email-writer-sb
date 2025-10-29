package com.email.writer.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailGeneratorController(EmailGeneratorService emailGeneratorService) {
        this.emailGeneratorService = emailGeneratorService;
    }

    @PostMapping("/generate")
    @CrossOrigin(origins = {"${app.frontend.url}", "chrome-extension://*", "*://mail.google.com"})
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest) {
        String response = emailGeneratorService.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Email Writer API is running!");
    }
    
    @PostMapping("/test")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> testGeneration() {
        EmailRequest testRequest = new EmailRequest();
        testRequest.setEmailContent("Hi, I wanted to follow up on our meeting yesterday. Could you please send me the documents we discussed?");
        testRequest.setTone("professional");
        
        String response = emailGeneratorService.generateEmailReply(testRequest);
        return ResponseEntity.ok(response);
    }
}

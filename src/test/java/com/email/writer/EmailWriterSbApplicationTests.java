package com.email.writer;

import com.email.writer.app.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {"GEMINI_KEY=test-key"})
class EmailWriterSbApplicationTests {

	private EmailContextService emailContextService;
	private EmailGeneratorController emailGeneratorController;

	@BeforeEach
	void setUp() {
		emailContextService = new EmailContextService();
	}

	@Test
	void contextLoads() {
		// Test that Spring context loads successfully
		assertNotNull(emailContextService);
	}

	@Test
	void testEmailContextService_detectTone_urgent() {
		String urgentEmail = "This is urgent! Please respond ASAP.";
		String tone = emailContextService.detectTone(urgentEmail);
		assertEquals("urgent", tone);
	}

	@Test
	void testEmailContextService_detectTone_grateful() {
		String gratefulEmail = "Thank you so much for your help. I really appreciate it.";
		String tone = emailContextService.detectTone(gratefulEmail);
		assertEquals("grateful", tone);
	}

	@Test
	void testEmailContextService_detectTone_apologetic() {
		String apologeticEmail = "I'm sorry for the mistake. I apologize for any inconvenience.";
		String tone = emailContextService.detectTone(apologeticEmail);
		assertEquals("apologetic", tone);
	}

	@Test
	void testEmailContextService_detectTone_scheduling() {
		String schedulingEmail = "Can we schedule a meeting for tomorrow?";
		String tone = emailContextService.detectTone(schedulingEmail);
		assertEquals("scheduling", tone);
	}

	@Test
	void testEmailContextService_detectTone_professional() {
		String professionalEmail = "Please find the attached document for your review.";
		String tone = emailContextService.detectTone(professionalEmail);
		assertEquals("professional", tone);
	}

	@Test
	void testEmailContextService_getContextualPrompt_urgent() {
		String prompt = emailContextService.getContextualPrompt("urgent email", "urgent");
		assertEquals("Respond with urgency and provide clear next steps.", prompt);
	}

	@Test
	void testEmailContextService_getContextualPrompt_grateful() {
		String prompt = emailContextService.getContextualPrompt("thank you email", "grateful");
		assertEquals("Acknowledge their thanks and maintain positive tone.", prompt);
	}

	@Test
	void testEmailContextService_getContextualPrompt_apologetic() {
		String prompt = emailContextService.getContextualPrompt("sorry email", "apologetic");
		assertEquals("Accept gracefully and focus on solutions.", prompt);
	}

	@Test
	void testEmailContextService_getContextualPrompt_scheduling() {
		String prompt = emailContextService.getContextualPrompt("meeting email", "scheduling");
		assertEquals("Be specific about availability and confirm details.", prompt);
	}

	@Test
	void testEmailContextService_getContextualPrompt_default() {
		String prompt = emailContextService.getContextualPrompt("normal email", "professional");
		assertEquals("Maintain professional courtesy.", prompt);
	}

	@Test
	void testEmailContextService_autoDetection() {
		// Test that requested tone overrides detected tone
		String emailContent = "Thank you for your email"; // Would detect as "grateful"
		String prompt = emailContextService.getContextualPrompt(emailContent, "professional");
		
		assertEquals("Maintain professional courtesy.", prompt);
	}

	@Test
	void testEmailContextService_fallbackToDetected() {
		// Test that detected tone is used when no tone is requested
		String emailContent = "This is urgent!";
		String prompt = emailContextService.getContextualPrompt(emailContent, null);
		
		assertEquals("Respond with urgency and provide clear next steps.", prompt);
	}

	@Test
	void testEmailContextService_fallbackToDetectedEmpty() {
		// Test that detected tone is used when tone is empty string
		String emailContent = "This is urgent!";
		String prompt = emailContextService.getContextualPrompt(emailContent, "");
		
		assertEquals("Respond with urgency and provide clear next steps.", prompt);
	}

	@Test
	void testEmailGeneratorController_health() {
		// Mock the service
		EmailGeneratorService mockService = mock(EmailGeneratorService.class);
		EmailGeneratorController controller = new EmailGeneratorController(mockService);
		
		ResponseEntity<String> response = controller.health();
		
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Email Writer API is running!", response.getBody());
	}

	@Test
	void testEmailGeneratorController_generateEmail() {
		// Mock the service to return a test response
		EmailGeneratorService mockService = mock(EmailGeneratorService.class);
		when(mockService.generateEmailReply(any(EmailRequest.class)))
			.thenReturn("Test generated response");
		
		EmailGeneratorController controller = new EmailGeneratorController(mockService);
		
		EmailRequest request = new EmailRequest();
		request.setEmailContent("Test email content");
		request.setTone("professional");
		
		ResponseEntity<String> response = controller.generateEmail(request);
		
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Test generated response", response.getBody());
		verify(mockService, times(1)).generateEmailReply(request);
	}

	@Test
	void testEmailGeneratorController_testGeneration() {
		// Mock the service
		EmailGeneratorService mockService = mock(EmailGeneratorService.class);
		when(mockService.generateEmailReply(any(EmailRequest.class)))
			.thenReturn("Test response for meeting follow-up");
		
		EmailGeneratorController controller = new EmailGeneratorController(mockService);
		
		ResponseEntity<String> response = controller.testGeneration();
		
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Test response for meeting follow-up", response.getBody());
		verify(mockService, times(1)).generateEmailReply(any(EmailRequest.class));
	}

	@Test
	void testEmailRequest_settersAndGetters() {
		EmailRequest request = new EmailRequest();
		
		request.setEmailContent("Test content");
		request.setTone("casual");
		
		assertEquals("Test content", request.getEmailContent());
		assertEquals("casual", request.getTone());
	}

	@Test
	void testEmailRequest_nullValues() {
		EmailRequest request = new EmailRequest();
		
		// Test with null values
		request.setEmailContent(null);
		request.setTone(null);
		
		assertNull(request.getEmailContent());
		assertNull(request.getTone());
	}

	@Test
	void testEmailContextService_caseInsensitive() {
		// Test that detection is case insensitive
		String urgentEmail = "THIS IS URGENT! PLEASE RESPOND ASAP.";
		String tone = emailContextService.detectTone(urgentEmail);
		assertEquals("urgent", tone);
	}

	@Test
	void testEmailContextService_multipleKeywords() {
		// Test email with multiple keywords - should return first match
		String mixedEmail = "Thank you for the urgent meeting request. I apologize for the delay.";
		String tone = emailContextService.detectTone(mixedEmail);
		assertEquals("urgent", tone); // "urgent" appears first in the detection order
	}
}

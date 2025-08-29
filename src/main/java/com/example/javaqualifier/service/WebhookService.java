package com.example.javaqualifier.service;

import com.example.javaqualifier.model.SubmitRequest;
import com.example.javaqualifier.model.WebhookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {

    private static final String GENERATE_WEBHOOK_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.name}")
    private String name;

    @Value("${app.regNo}")
    private String regNo;

    @Value("${app.email}")
    private String email;

    public void startProcess() {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("name", name);
            body.put("regNo", regNo);
            body.put("email", email);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<WebhookResponse> response =
                    restTemplate.exchange(GENERATE_WEBHOOK_URL,
                            HttpMethod.POST, request, WebhookResponse.class);

            WebhookResponse webhookResponse = response.getBody();
            if (webhookResponse == null) {
                System.out.println("Failed to get webhook response!");
                return;
            }

            System.out.println("Webhook: " + webhookResponse.getWebhook());
            System.out.println("AccessToken: " + (webhookResponse.getAccessToken()==null?"null":"[REDACTED]")); 

            // decide question based on regNo last two digits
            String finalQuery = "";
            String cleaned = regNo.replaceAll("[^0-9]", "");
            int lastTwo = 0;
            if (cleaned.length() >= 2) {
                lastTwo = Integer.parseInt(cleaned.substring(cleaned.length() - 2));
            } else if (cleaned.length() == 1) {
                lastTwo = Integer.parseInt(cleaned);
            }

            if (lastTwo % 2 == 0) {
                // Placeholder - replace with actual SQL for Question 2
                finalQuery = "SELECT * FROM sample_table WHERE id > 100;"; 
            } else {
                // Placeholder - replace with actual SQL for Question 1
                finalQuery = "SELECT * FROM sample_table WHERE status = 'ACTIVE';";
            }

            // store result locally
            Path outDir = Path.of("out");
            Files.createDirectories(outDir);
            Files.writeString(outDir.resolve("finalQuery.sql"), finalQuery);

            // submit final query
            SubmitRequest submitRequest = new SubmitRequest(finalQuery);

            HttpHeaders submitHeaders = new HttpHeaders();
            submitHeaders.setContentType(MediaType.APPLICATION_JSON);
            submitHeaders.setBearerAuth(webhookResponse.getAccessToken());

            HttpEntity<SubmitRequest> submitEntity = new HttpEntity<>(submitRequest, submitHeaders);

            ResponseEntity<String> submitResponse =
                    restTemplate.exchange(webhookResponse.getWebhook(),
                            HttpMethod.POST, submitEntity, String.class);

            System.out.println("Submission Response: " + submitResponse.getStatusCode() + " - " + submitResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

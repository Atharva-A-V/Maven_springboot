package com.example.javaapp.service;

import com.example.javaapp.model.GenerateWebhookRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void startFlow() {
        try {
            String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            GenerateWebhookRequest request = new GenerateWebhookRequest(
                    "Atharva Vaishampayan",     
                    "22BCE2886",      
                    "atharva.anand2022@vitstudent.ac.in" 
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(generateUrl, request, Map.class);
            Map body = response.getBody();

            if (body == null) {
                System.out.println("No response from webhook generation.");
                return;
            }

            String webhookUrl = (String) body.get("webhook");
            String accessToken = (String) body.get("accessToken");

            System.out.println("Webhook URL: " + webhookUrl);
            System.out.println("Access Token: " + accessToken);

            
            String finalQuery =
                    "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME, " +
                    "COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
                    "FROM EMPLOYEE e " +
                    "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                    "LEFT JOIN EMPLOYEE e2 ON e.DEPARTMENT = e2.DEPARTMENT " +
                    "AND e2.DOB > e.DOB " +
                    "GROUP BY e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME " +
                    "ORDER BY e.EMP_ID DESC;";

            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            Map<String, String> solution = Map.of("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(solution, headers);

            ResponseEntity<String> submitResponse =
                    restTemplate.postForEntity(webhookUrl, entity, String.class);

            System.out.println("Submit Response: " + submitResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
